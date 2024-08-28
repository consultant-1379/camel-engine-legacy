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

import org.junit.*;

import com.ericsson.oss.mediation.camel.jca.ra.CamelRaMetaData;
import com.ericsson.oss.mediation.camel.jca.ra.CamelResourceAdapter;

public class CamelRaMetaDataTest {

    private CamelRaMetaData raMetaData;
    private static final String RA_NAME = "Camel Resource Adapter";
    private static final String RA_VERSION = "1.0";
    private static final String RA_VENDOR = "Ericsson";
    private static final String RA_SHORT_DESC = "Camel resource adapter";
    private static final String RA_SPEC_VERSION = "1.6";

    @Before
    public void setUp() {
	final CamelResourceAdapter radapter = new CamelResourceAdapter();
	radapter.setName(RA_NAME);
	radapter.setVersion(RA_VERSION);
	this.raMetaData = new CamelRaMetaData(radapter);
    }

    @Test
    public void testRaNameMetadata() {
	Assert.assertEquals(RA_NAME, raMetaData.getAdapterName());
    }

    @Test
    public void testRaVersionMetadata() {
	Assert.assertEquals(RA_VERSION, raMetaData.getAdapterVersion());
    }

    @Test
    public void testRaVendorName() {
	Assert.assertEquals(RA_VENDOR, raMetaData.getAdapterVendorName());
    }

    @Test
    public void testRaShortDescription() {
	Assert.assertEquals(RA_SHORT_DESC, raMetaData.getAdapterShortDescription());
    }

    @Test
    public void testRaConnectorVersion() {
	Assert.assertEquals(RA_SPEC_VERSION, raMetaData.getSpecVersion());
    }

    @Test
    public void testRaInteractionSpecsSupported() {
	final String[] supportedSpecs = raMetaData.getInteractionSpecsSupported();

	Assert.assertTrue((supportedSpecs.length == 0));
    }

    @Test
    public void testSupportsExecuteWithInputAndOutputRecord() {
	Assert.assertFalse(this.raMetaData.supportsExecuteWithInputAndOutputRecord());
    }

    @Test
    public void testSupportsExecuteWithInputRecordOnly() {
	Assert.assertFalse(this.raMetaData.supportsExecuteWithInputRecordOnly());
    }

    @Test
    public void testSupportsLocalTransactionDemarcation() {
	Assert.assertFalse(this.raMetaData.supportsLocalTransactionDemarcation());
    }

    /**
     * @return the raMetaData
     */
    public CamelRaMetaData getRaMetaData() {
	return raMetaData;
    }

    /**
     * @param raMetaData
     *            the raMetaData to set
     */
    public void setRaMetaData(final CamelRaMetaData raMetaData) {
	this.raMetaData = raMetaData;
    }

}
