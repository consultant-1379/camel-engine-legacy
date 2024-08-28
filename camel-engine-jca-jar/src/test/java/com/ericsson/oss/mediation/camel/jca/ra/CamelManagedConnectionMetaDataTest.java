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

import javax.resource.ResourceException;

import org.junit.*;

public class CamelManagedConnectionMetaDataTest {

    private CamelManagedConnectionMetaData metaData;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.metaData = new CamelManagedConnectionMetaData();

    }

    /**
     * Test method for
     * {@link com.ericsson.nms.mediation.mediationservice.camel.jca.ra.CamelManagedConnectionMetaData#getEISProductName()}
     * .
     */
    @Test
    public void testGetEISProductName() throws ResourceException {
        Assert.assertNull(this.metaData.getEISProductName());
    }

    /**
     * Test method for
     * {@link com.ericsson.nms.mediation.mediationservice.camel.jca.ra.CamelManagedConnectionMetaData#getEISProductVersion()}
     * .
     */
    @Test
    public void testGetEISProductVersion() throws ResourceException {
        Assert.assertNull(this.metaData.getEISProductVersion());
    }

    /**
     * Test method for
     * {@link com.ericsson.nms.mediation.mediationservice.camel.jca.ra.CamelManagedConnectionMetaData#getMaxConnections()}
     * .
     */
    @Test
    public void testGetMaxConnections() throws ResourceException {
        Assert.assertEquals(1, this.metaData.getMaxConnections());
    }

    /**
     * Test method for
     * {@link com.ericsson.nms.mediation.mediationservice.camel.jca.ra.CamelManagedConnectionMetaData#getUserName()}
     * .
     */
    @Test
    public void testGetUserName() throws ResourceException {
        Assert.assertNull(this.metaData.getUserName());
    }

}