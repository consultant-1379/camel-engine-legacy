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

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.spi.work.WorkManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CamelManagedConnectionTest {

    CamelManagedConnection camelConnection = null;
    WorkManager wm = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUpBeforeClass() throws Exception {
        CamelManagedConnectionFactory mcf = Mockito
                .mock(CamelManagedConnectionFactory.class);
        CamelConnectionRequestInfo cxRequestInfo = Mockito
                .mock(CamelConnectionRequestInfo.class);
        CamelResourceAdapter ra = Mockito.mock(CamelResourceAdapter.class);
        wm = Mockito.mock(WorkManager.class);
        when(ra.getWorkManager()).thenReturn(wm);
        when(mcf.getResourceAdapter()).thenReturn(ra);
        this.camelConnection = new CamelManagedConnection(mcf, cxRequestInfo);
    }

    /**
     * Test method for
     * {@link com.ericsson.oss.mediation.camel.jca.ra.CamelManagedConnection#applyInput(java.lang.String, java.util.Map)}
     * .
     * 
     * @throws ResourceException
     */
    @Test
    public void testApplyInput() throws ResourceException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("testPathId|port", "1234");
        camelConnection.applyInput("testPathId", headers);
    }

    @Test
    public void testResolve() throws ResourceException {
        final String result = "port";
        Map<String, Object> headers = new HashMap<>();
        headers.put("testPathId|port", "1234");
        Map<String, Object> results = camelConnection.resolveHeadersID(headers);
        Assert.assertEquals(true, results.containsKey(result));
        Assert.assertEquals("1234", results.get(result));
    }

}

