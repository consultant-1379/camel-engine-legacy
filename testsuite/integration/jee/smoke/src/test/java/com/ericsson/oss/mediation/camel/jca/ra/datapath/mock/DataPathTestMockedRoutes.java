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
package com.ericsson.oss.mediation.camel.jca.ra.datapath.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;
import com.ericsson.oss.mediation.datapath.definition.element.Element;

public class DataPathTestMockedRoutes {

    /**
     * Empty datapath - no elements
     */
    public static final String EMPTY_DATAPATH_ID = "com.ericsson.oss.mediation.emptyDatapath";

    /**
     * Test processors
     * 
     */

    public static List<DataPathDefinition> createDPWithNoElements() {

        List<DataPathDefinition> dpDefinitionList = new ArrayList<>();
        List<Element> dpElementList = new ArrayList<>();
        DataPathDefinition singleDataPath = mock(DataPathDefinition.class);
        when(singleDataPath.getEngine()).thenReturn("camel");
        when(singleDataPath.getId()).thenReturn(EMPTY_DATAPATH_ID);
        when(singleDataPath.getErrorHandlerReference()).thenReturn(null);
        when(singleDataPath.getRedeliveryAttempts()).thenReturn(0);
        when(singleDataPath.getElements()).thenReturn(dpElementList);
        /**
         * add elements of datapath
         */

        dpDefinitionList.add(singleDataPath);
        return dpDefinitionList;

    }

}
