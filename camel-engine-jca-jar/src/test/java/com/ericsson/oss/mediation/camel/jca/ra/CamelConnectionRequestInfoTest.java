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

import java.util.LinkedList;
import java.util.List;

import org.junit.*;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;

public class CamelConnectionRequestInfoTest {

    private CamelConnectionRequestInfo req;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        final DataPathDefinition dpDef = mock(DataPathDefinition.class);
        List<DataPathDefinition> defList = new LinkedList<>();
        defList.add(dpDef);
        this.req = new CamelConnectionRequestInfo(defList);
    }

    /**
     * Test method for
     * {@link com.ericsson.nms.mediation.mediationservice.camel.jca.ra.CamelConnectionRequestInfo#CamelConnectionRequestInfo(com.ericsson.nms.mediation.datapath.api.DataPathDefinition)}
     * .
     */
    @Test
    public void testCamelConnectionRequestInfo() {
        Assert.assertNotNull(this.req.getPathDef());
    }

    /**
     * Test method for
     * {@link com.ericsson.nms.mediation.mediationservice.camel.jca.ra.CamelConnectionRequestInfo#getPathDef()}
     * .
     */
    @Test
    public void testGetDataPathDef() {
        Assert.assertNotNull(this.req.getPathDef());
    }

}
