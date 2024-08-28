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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.resource.ResourceException;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.camel.components.eftp.exceptions.GenericEftpException;
import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;
import com.ericsson.oss.mediation.datapath.definition.element.*;

/**
 * Tests the CamelRouteBuilder from a JUnit perspective.
 * 
 * These tests will have no dependencies to how DataPath definitions are
 * resolved. It should be up to other unit tests to ensure that that is done
 * correctly.
 * 
 * The main data path set up expects a route of this format:
 * 
 * from("direct:dataPathUniqueID").to(processorOne).to("mock:endpoint").to(
 * processorTwo)
 */
@RunWith(MockitoJUnitRunner.class)
public class CamelRouteBuilderTest extends CamelTestSupport {

    // the generated camel context
    private CamelContext camelContext;
    // our test class
    private CamelRouteBuilder routeBuilder;

    private static DataPathDefinition pathDefinition;

    private static final String dataPathID = "dataPathUniqueID";

    @EndpointInject(uri = "mock:endpoint")
    protected MockEndpoint resultEndpoint;

    private static String errorPathId = "myErrorHandler";
    @EndpointInject(uri = "mock:errors")
    protected MockEndpoint errorsEndpoint;

    @Produce(uri = "direct:dataPathUniqueID")
    protected ProducerTemplate template;

    private static TrackingProcessor processorOne = new TrackingProcessor();
    private static TrackingProcessor processorTwo = new TrackingProcessor();

    /**
     * Has to be done before class to ensure mocks are initialised before route
     * is constructed.
     */
    @BeforeClass
    public static void setUpDataPaths() {
        pathDefinition = mock(DataPathDefinition.class);
        when(pathDefinition.getId()).thenReturn(dataPathID);
        final List<Element> elements = getMockedDataPathElements();
        when(pathDefinition.getElements()).thenReturn(elements);
        when(pathDefinition.getErrorHandlerReference()).thenReturn(errorPathId);
    }

    private static List<Element> getMockedDataPathElements() {
        final List<Element> elements = new ArrayList<>();
        final Uri startPoint = getUri("direct:" + dataPathID);
        elements.add(startPoint);
        final ProcessorElement processorElementOne = getProcessorElement(processorOne);
        elements.add(processorElementOne);
        final Uri endPoint = getUri("mock:endpoint");
        elements.add(endPoint);
        final ProcessorElement processorElementTwo = getProcessorElement(processorTwo);
        elements.add(processorElementTwo);
        return elements;
    }

    private static ProcessorElement getProcessorElement(
            final TrackingProcessor processor) {
        final ProcessorElement element = mock(ProcessorElement.class);
        when(element.getType()).thenReturn(ElementType.PROCESSOR);
        when(element.getProcessorInstance()).thenReturn(
                processor.getClass().getCanonicalName());
        return element;
    }

    private static Uri getUri(final String uriString) {
        final Uri uri = mock(Uri.class);
        when(uri.getType()).thenReturn(ElementType.URI);
        when(uri.getUriString()).thenReturn(uriString);
        return uri;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        camelContext = super.createCamelContext();
        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        routeBuilder = new CamelRouteBuilder(pathDefinition);
        return new ErrorDefiningRouteBuilder(routeBuilder);
    }

    @Override
    public boolean isCreateCamelContextPerClass() {
        return false;
    }

    @Test(expected = ResourceException.class)
    public void configure_DataPathWithNoElementsThrowsException()
            throws ResourceException {
        final DataPathDefinition dataPathDefinitionNoElements = mock(DataPathDefinition.class);
        when(dataPathDefinitionNoElements.getElements()).thenReturn(
                Collections.<Element> emptyList());
        routeBuilder = new CamelRouteBuilder(dataPathDefinitionNoElements);
        routeBuilder.configure();
    }

    /**
     * Confirms the data gets routed through the mocked end point.
     */
    @Test
    public void sendExchange_GetsRoutedToEndPoint() throws InterruptedException {
        final String expectedBody = "<matched/>";

        resultEndpoint.expectedBodiesReceived(expectedBody);

        template.sendBodyAndHeader(expectedBody, "foo", "bar");

        resultEndpoint.assertIsSatisfied();
    }

    /**
     * Confirms the data gets routed through the mocked end point.
     */
    @Test
    @Ignore("test ignored as depricated, can't test this way anymore")
    public void sendExchange_GetsRoutedThroughProcessors() {
        final String expectedBody = "<matched/>";

        processorOne.setExpectedBody(expectedBody);
        processorTwo.setExpectedBody(expectedBody);

        template.sendBodyAndHeader(expectedBody, "foo", "bar");

        processorOne.verify();
        processorTwo.verify();
    }

    @Test
    public void sendExchange_DoesNotGetSentToErrorPathForNormalCase()
            throws InterruptedException {

        errorsEndpoint.expectedMessageCount(0);

        template.sendBodyAndHeader("", "foo", "bar");

        errorsEndpoint.assertIsSatisfied();
    }

    @Test
    @Ignore("test ignored as depricated, can't test this way anymore")
    public void sendExchange_ThrowEftpException_ErrorHandlerDefined_ExceptionSentToErrorPath()
            throws Exception {

        final GenericEftpException eftpException = new GenericEftpException(
                430, "connection failed");

        processorOne.throwException(eftpException);

        errorsEndpoint.expectedMessageCount(1);

        template.sendBodyAndHeader("", "foo", "bar");

        errorsEndpoint.assertIsSatisfied();

    }

    class ErrorDefiningRouteBuilder extends RouteBuilder {

        private final RouteBuilder camelBuilder;

        public ErrorDefiningRouteBuilder(final RouteBuilder otherBuilder) {
            this.camelBuilder = otherBuilder;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void configure() throws Exception {
            from("direct:" + errorPathId).to("mock:errors");
            this.addRoutes(camelBuilder);
        }

    }
}
