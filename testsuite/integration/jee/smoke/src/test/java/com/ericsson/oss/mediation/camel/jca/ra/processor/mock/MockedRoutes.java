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
package com.ericsson.oss.mediation.camel.jca.ra.processor.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;
import com.ericsson.oss.mediation.datapath.definition.element.*;

public class MockedRoutes {

    /**
     * Datapaths used in tests
     */
    public static final String PROCESSOR_DATAPATH_ID = "com.ericsson.oss.mediation.testProcessorDatapath";
    public static final String NO_DEF_CONSTRUCTOR_PROCESSOR_DATAPATH_ID = "com.ericsson.oss.mediation.NoDefaultConstructorDataPath";
    public static final String PROCESSOR_NOT_ON_CLASSPATH_DATAPATH_ID = "com.ericsson.oss.mediation.testProcessorNotOnClassPathDatapath";

    /**
     * Test processors
     * 
     */

    public static final String NONEXISTING_PROCESSOR = "com.ericsson.oss.mediation.sets.pm.processors.ProcessorNotOnClasspath";

    public static List<DataPathDefinition> createDPWithProcessor() {

        List<DataPathDefinition> dpDefinitionList = new ArrayList<>();
        List<Element> dpElementList = new ArrayList<>();
        DataPathDefinition singleDataPath = mock(DataPathDefinition.class);
        when(singleDataPath.getEngine()).thenReturn("camel");
        when(singleDataPath.getId()).thenReturn(PROCESSOR_DATAPATH_ID);
        when(singleDataPath.getErrorHandlerReference()).thenReturn(null);
        when(singleDataPath.getRedeliveryAttempts()).thenReturn(0);
        when(singleDataPath.getElements()).thenReturn(dpElementList);
        /**
         * add elements of datapath
         */
        final Uri from = mock(Uri.class);
        when(from.getType()).thenReturn(ElementType.URI);
        when(from.getId()).thenReturn(PROCESSOR_DATAPATH_ID);
        when(from.getUriString()).thenReturn("direct:" + PROCESSOR_DATAPATH_ID);
        dpElementList.add(from);

        final ProcessorElement processor = mock(ProcessorElement.class);
        when(processor.getId()).thenReturn(
                "com.ericsson.oss.mediation.testProcessor");
        when(processor.getType()).thenReturn(ElementType.PROCESSOR);
        when(processor.getProcessorInstance()).thenReturn(
                "com.ericsson.oss.mediation.sets.pm.processors.EftpProcessor");
        dpElementList.add(processor);
        dpDefinitionList.add(singleDataPath);
        return dpDefinitionList;

    }

    public static List<DataPathDefinition> createDPWithProcessorNotOnClassPath() {

        List<DataPathDefinition> dpDefinitionList = new ArrayList<>();
        List<Element> dpElementList = new ArrayList<>();
        DataPathDefinition singleDataPath = mock(DataPathDefinition.class);
        when(singleDataPath.getEngine()).thenReturn("camel");
        when(singleDataPath.getId()).thenReturn(
                PROCESSOR_NOT_ON_CLASSPATH_DATAPATH_ID);
        when(singleDataPath.getErrorHandlerReference()).thenReturn(null);
        when(singleDataPath.getRedeliveryAttempts()).thenReturn(0);
        when(singleDataPath.getElements()).thenReturn(dpElementList);
        /**
         * add elements of datapath
         */
        final Uri from = mock(Uri.class);
        when(from.getType()).thenReturn(ElementType.URI);
        when(from.getId()).thenReturn(PROCESSOR_NOT_ON_CLASSPATH_DATAPATH_ID);
        when(from.getUriString()).thenReturn(
                "direct:" + PROCESSOR_NOT_ON_CLASSPATH_DATAPATH_ID);
        dpElementList.add(from);

        final ProcessorElement processor = mock(ProcessorElement.class);
        when(processor.getId()).thenReturn(NONEXISTING_PROCESSOR);
        when(processor.getType()).thenReturn(ElementType.PROCESSOR);
        when(processor.getProcessorInstance())
                .thenReturn(NONEXISTING_PROCESSOR);
        dpElementList.add(processor);
        dpDefinitionList.add(singleDataPath);
        return dpDefinitionList;

    }

    /**
     * Create dp with processor that has no default constructor
     * 
     * @return
     */
    public static List<DataPathDefinition> createDPWithProcessorWithoutDefaultConstructor() {

        List<DataPathDefinition> dpDefinitionList = new ArrayList<>();
        List<Element> dpElementList = new ArrayList<>();
        DataPathDefinition singleDataPath = mock(DataPathDefinition.class);
        when(singleDataPath.getEngine()).thenReturn("camel");
        when(singleDataPath.getId()).thenReturn(
                NO_DEF_CONSTRUCTOR_PROCESSOR_DATAPATH_ID);
        when(singleDataPath.getErrorHandlerReference()).thenReturn(null);
        when(singleDataPath.getRedeliveryAttempts()).thenReturn(0);
        when(singleDataPath.getElements()).thenReturn(dpElementList);
        /**
         * add elements of datapath
         */
        final Uri from = mock(Uri.class);
        when(from.getType()).thenReturn(ElementType.URI);
        when(from.getId()).thenReturn(NO_DEF_CONSTRUCTOR_PROCESSOR_DATAPATH_ID);
        when(from.getUriString()).thenReturn(
                "direct:" + NO_DEF_CONSTRUCTOR_PROCESSOR_DATAPATH_ID);
        dpElementList.add(from);

        final ProcessorElement processor = mock(ProcessorElement.class);
        when(processor.getId()).thenReturn(NONEXISTING_PROCESSOR);
        when(processor.getType()).thenReturn(ElementType.PROCESSOR);
        when(processor.getProcessorInstance()).thenReturn(
                DummyProcessorWithNoPublicConstructor.class.getCanonicalName());
        dpElementList.add(processor);
        dpDefinitionList.add(singleDataPath);
        return dpDefinitionList;

    }
}
