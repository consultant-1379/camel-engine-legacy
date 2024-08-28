/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.camel.jca.ra;

import static org.apache.camel.builder.ExpressionBuilder.beanExpression;

import java.util.Iterator;

import javax.resource.ResourceException;

import org.apache.camel.Processor;
import org.apache.camel.builder.*;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SplitDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;
import com.ericsson.oss.mediation.datapath.definition.element.*;

/**
 * Custom implementation of camel RouteBuilder abstract class, used to drive
 * creation of route from parsed XML definition of our routes
 */
public class CamelRouteBuilder extends RouteBuilder {

    private static final Logger log = LoggerFactory
            .getLogger(CamelRouteBuilder.class);

    private final DataPathDefinition pathDef;

    public CamelRouteBuilder(final DataPathDefinition pathDefinition) {
        this.pathDef = pathDefinition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws ResourceException {
        try {

            log.debug("configuring route with routeId=[{}]",
                    this.pathDef.getId());

            final Iterator<Element> elementIterator = this.pathDef
                    .getElements().iterator();
            if (!elementIterator.hasNext()) {
                throw new ResourceException(
                        "Unable to create camel route, datapath element list is empty.");
            }
            final Uri fromUri = (Uri) elementIterator.next();
            final RouteDefinition routeDef = from(fromUri.getUriString());
            routeDef.setId(this.pathDef.getId());
            SplitDefinition splitDef = null;
            Boolean concatOnSplit = false;
            addErrorHandlerIfNeeded(routeDef);
            while (elementIterator.hasNext()) {
                final Element element = elementIterator.next();
                switch (element.getType()) {
                case PROCESSOR:
                    if (concatOnSplit) {
                        processProcessorToSplitter((ProcessorElement) element,
                                splitDef);
                        concatOnSplit = false;
                    } else {
                        processProcessor((ProcessorElement) element, routeDef);
                    }
                    break;

                case SPLITTER:
                    log.debug("Found splitter element: {}", element.getId());
                    splitDef = processSplitter((SplitterElement) element,
                            routeDef);
                    concatOnSplit = true;
                    break;

                case REFERENCE:
                    log.debug("Found reference: {}", element.getId());
                    routeDef.to("direct:" + element.getId());
                    break;

                case URI:
                    log.debug("Found URI element: {}", element.getId());
                    if (concatOnSplit) {
                        log.debug("Adding .to element onto splitter...");
                        processToSplitter((Uri) element, splitDef);
                        concatOnSplit = false;
                    } else {
                        log.debug("Adding .to element onto route definition ...");
                        processTo((Uri) element, routeDef, splitDef,
                                concatOnSplit);
                    }
                    break;

                }
            }

        } catch (final Exception e) {
            log.error(
                    "Exception caught while trying to construct camel route, stack trace is ",
                    e);
            throw new ResourceException(e.getMessage());
        }
        log.debug("Route structure on build complete, [{}]", this.toString());

    }

    private void addErrorHandlerIfNeeded(final RouteDefinition routeDef) {
        final String errorHandlerRef = pathDef.getErrorHandlerReference();
        if (errorHandlerRef != null) {
            final String errorURI = "direct:" + errorHandlerRef;
            final DefaultErrorHandlerBuilder errorHandler = deadLetterChannel(errorURI);
            final ErrorHandlerBuilder redeliveryBuilder = setRedeliveryStrategy(errorHandler);
            routeDef.errorHandler(redeliveryBuilder);
        }
    }

    private ErrorHandlerBuilder setRedeliveryStrategy(
            final DefaultErrorHandlerBuilder errorHandler) {
        ErrorHandlerBuilder redeliveryHandler;
        final int redeliveryAttempts = pathDef.getRedeliveryAttempts();
        if (redeliveryAttempts == 0) {
            redeliveryHandler = errorHandler.disableRedelivery();
        } else {
            redeliveryHandler = errorHandler
                    .maximumRedeliveries(redeliveryAttempts);
        }
        return redeliveryHandler;
    }

    /**
     * Utility method that will process a "to" element types
     * 
     * @param uriElement
     *            Data Path URI Element
     * @param routeDef
     *            RouteDefinition where the element will be added
     */
    private void processTo(final Uri uriElement,
            final RouteDefinition routeDef, final SplitDefinition splitDef,
            Boolean concatOnSplit) {
        log.debug("Found EndPoint, adding end point =[{}]",
                uriElement.getUriString());
        if (concatOnSplit) {
            log.debug("Element will be added to splitter as child element {}",
                    uriElement.getUriString());
            splitDef.to(uriElement.getUriString());
            concatOnSplit = false;
        } else {
            log.debug("Element {} will be added to route definition",
                    uriElement.getUriString());
            routeDef.to(uriElement.getUriString());
        }
    }

    /**
     * Utility method that will process a <code>"to"</code> element types and
     * add them into splitter as child element.
     * 
     * @param uriElement
     *            Data Path URI Element
     * @param splitDef
     *            SplitDefinition where the element will be added
     */
    private void processToSplitter(final Uri uriElement,
            final SplitDefinition splitDef) {
        log.debug(
                "Found EndPoint, adding end point to splitter definition =[{}]",
                uriElement.getUriString());
        splitDef.to(uriElement.getUriString());
    }

    /**
     * Utility method that will process <code>Processor</code> types and add
     * them to splitter as child element
     * 
     * @param element
     *            Data Path Element
     * @param routeDef
     *            SplitDefinition where the processor will be added
     */
    private void processProcessorToSplitter(final ProcessorElement element,
            final SplitDefinition splitDef) {
        final Processor processorInstance = getInstance(element
                .getProcessorInstance());
        log.debug("Found processor type, adding processor to splitter =[{}]",
                processorInstance.getClass().getCanonicalName());
        splitDef.process(processorInstance);
    }

    /**
     * Utility method that will process <code>Processor</code> types
     * 
     * @param element
     *            Data Path Element
     * @param routeDef
     *            RouteDefinition where the processor will be added
     */
    private void processProcessor(final ProcessorElement element,
            final RouteDefinition routeDef) {
        final Processor processorInstance = getInstance(element
                .getProcessorInstance());
        log.debug("Found processor type, adding processor=[{}]",
                processorInstance.getClass().getCanonicalName());
        routeDef.process(processorInstance);
    }

    /**
     * Utility method that will process <code>Splitter</code> types
     * 
     * @param element
     *            Splitter Element
     * @param routeDef
     *            RouteDefinition where the splitter will be added
     */
    private SplitDefinition processSplitter(final SplitterElement element,
            final RouteDefinition routeDef) {
        final String splitterClassName = element.getSplitterBeanClassName();
        log.debug("Found splitter type, adding splitter=[{}]",
                splitterClassName);
        final Object splitterBeanInstance = getSplitterBeanInstance(splitterClassName);
        return routeDef.split(beanExpression(splitterBeanInstance,
                element.getMethod()));
    }

    private Processor getInstance(final String className) {
        try {

            log.trace("Trying to load processor using class loader {}", Thread
                    .currentThread().getContextClassLoader());
            final Class<?> processorClass = Thread.currentThread()
                    .getContextClassLoader().loadClass(className);
            if (Processor.class.isAssignableFrom(processorClass)) {
                return (Processor) processorClass.newInstance();
            } else {
                throw new IllegalStateException(
                        "Class "
                                + className
                                + " is not implementing org.apache.camel.Processor interface, check your implementation of the processor.");
            }
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Processor class " + className
                    + " was not found on classpath.");
        } catch (final InstantiationException ine) {
            throw new IllegalStateException(
                    "Unable to create processor instance of type " + className
                            + " processor must have default constructor.");
        } catch (final IllegalAccessException ie) {
            throw new IllegalStateException(
                    "Unable to create processor instance of type "
                            + className
                            + " processor must have visible default constructor.");
        }
    }

    public Object getSplitterBeanInstance(final String className) {
        try {
            log.trace("Trying to load splitter bean using class loader {}",
                    Thread.currentThread().getContextClassLoader());
            final Class<?> splitterBeanClass = Thread.currentThread()
                    .getContextClassLoader().loadClass(className);
            return splitterBeanClass.newInstance();
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Cannot find splitter bean with class name " + className);
        } catch (final InstantiationException ine) {
            throw new IllegalStateException(
                    "Cannot instantiate splitter bean with class name  "
                            + className
                            + " , splitter bean must have default constructor, check your splitter bean implementation.");
        } catch (final IllegalAccessException ie) {
            throw new IllegalStateException(
                    "Cannot instantiate splitter bean with class name "
                            + className
                            + ", splitter bean must have visible default constructor, check your splitter bean implementation.");
        }
    }
}
