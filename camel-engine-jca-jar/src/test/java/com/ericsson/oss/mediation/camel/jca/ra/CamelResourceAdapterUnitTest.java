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

import javax.resource.spi.work.WorkManager;

import org.apache.camel.impl.DefaultCamelContext;
import org.junit.*;
import org.mockito.Mockito;

/**
 * @author edejket
 * 
 */
public class CamelResourceAdapterUnitTest {

    private CamelResourceAdapter camelRAdapter;
    public static final String RA_NAME = "CamelRA";
    public static final String RA_VERSION = "1.0";

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        WorkManager wm = Mockito.mock(WorkManager.class);
        camelRAdapter = new CamelResourceAdapter();
        camelRAdapter.setName(RA_NAME);
        camelRAdapter.setVersion(RA_VERSION);
        camelRAdapter.setCamelContext(new DefaultCamelContext());
        camelRAdapter.setWorkManager(wm);
    }

    @After
    public void tearDown() throws Exception {
        if (this.camelRAdapter != null) {
            if (this.camelRAdapter.getCamelContext() != null) {
                if (this.camelRAdapter.getCamelContext().getStatus()
                        .isStarted()) {
                    this.camelRAdapter.getCamelContext().stop();
                }
            }
        }
    }

    @Test
    public void testSetName() {
        Assert.assertEquals(RA_NAME, camelRAdapter.getName());
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(RA_NAME, camelRAdapter.getName());
    }

    @Test
    public void testSetVersion() {
        Assert.assertEquals(RA_VERSION, camelRAdapter.getVersion());
    }

    @Test
    public void testGetVersion() {
        Assert.assertEquals(RA_VERSION, camelRAdapter.getVersion());
    }

    @Test
    public void testgenHashCode() {
        Assert.assertEquals(-85079857, camelRAdapter.hashCode());
    }

    @Test
    public void camelResourceAdapterEqualsTrue() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(RA_VERSION);
        Assert.assertTrue(camelRAdapter.equals(cra));

    }

    @Test
    public void camelResourceAdapterEqualsFalse() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion("SomeOtherVersion");
        Assert.assertFalse(camelRAdapter.equals(cra));
    }

    @Test
    public void camelResourceAdapterEqualsNullIsFalse() {
        Assert.assertFalse(camelRAdapter.equals(null));
    }

    @Test
    public void camelResourceAdapterEqualsSelf() {
        Assert.assertTrue(camelRAdapter.equals(this.camelRAdapter));
    }

    @Test
    public void camelResourceAdapterEqualsFalseWhenWrongClass() {
        Assert.assertFalse(camelRAdapter.equals(this));
    }

    @Test
    public void camelResourceAdapterEqualsFalseNameIsNull() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(null);
        Assert.assertFalse(camelRAdapter.equals(this));
    }

    @Test
    public void camelResourceAdapterEqualsNameIsNotNull() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(RA_VERSION);
        this.camelRAdapter.setName(RA_NAME + "Some more test");
        Assert.assertFalse(camelRAdapter.equals(this));
    }

    @Test
    public void camelResourceAdapterEqualsFalseOtherNameIsNotNull() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(RA_VERSION);
        this.camelRAdapter.setName(null);
        Assert.assertFalse(camelRAdapter.equals(this));
    }

    @Test
    public void camelResourceAdapterEqualsFalseVersionIsNull() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(null);
        Assert.assertFalse(camelRAdapter.equals(this));
    }

    @Test
    public void camelResourceAdapterEqualsFalseVersionIsNotNull() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(RA_VERSION);
        this.camelRAdapter.setVersion(null);
        Assert.assertFalse(camelRAdapter.equals(this));
    }

    @Test
    public void camelResourceAdapterEqualsVersionIsNotNull() {
        final CamelResourceAdapter cra = new CamelResourceAdapter();
        cra.setName(RA_NAME);
        cra.setVersion(RA_VERSION + "Some more test");
        this.camelRAdapter.setName(RA_NAME);
        Assert.assertFalse(camelRAdapter.equals(this));
    }

}
