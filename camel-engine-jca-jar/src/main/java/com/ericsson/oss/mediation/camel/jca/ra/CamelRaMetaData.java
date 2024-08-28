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

import javax.resource.cci.ResourceAdapterMetaData;

/**
 * CamelRaMetaData
 * 
 * @version $Revision: $
 */
public class CamelRaMetaData implements ResourceAdapterMetaData {

    private final String adapterVersion;
    private final String adapterVendor;
    private final String adapterName;
    private final String adapterShortDesc;

    private static final String connectorVersion = "1.6";

    public CamelRaMetaData(final CamelResourceAdapter resAdapter) {
        this.adapterVersion = resAdapter.getVersion();
        this.adapterVendor = "Ericsson";
        this.adapterName = resAdapter.getName();
        this.adapterShortDesc = "Camel resource adapter";

    }

    /**
     * Gets the version of the resource adapter.
     * 
     * @return String representing version of the resource adapter
     */
    @Override
    public String getAdapterVersion() {
        return this.adapterVersion;
    }

    /**
     * Gets the name of the vendor that has provided the resource adapter.
     * 
     * @return String representing name of the vendor
     */
    @Override
    public String getAdapterVendorName() {
        return this.adapterVendor;
    }

    /**
     * Gets a tool displayable name of the resource adapter.
     * 
     * @return String representing the name of the resource adapter
     */
    @Override
    public String getAdapterName() {
        return this.adapterName;
    }

    /**
     * Gets a tool displayable short desription of the resource adapter.
     * 
     * @return String describing the resource adapter
     */
    @Override
    public String getAdapterShortDescription() {
        return this.adapterShortDesc;
    }

    /**
     * Returns a string representation of the version
     * 
     * @return String representing the supported version of the connector
     *         architecture
     */
    @Override
    public String getSpecVersion() {
        return connectorVersion;
    }

    /**
     * Returns an array of fully-qualified names of InteractionSpec
     * 
     * @return Array of fully-qualified class names of InteractionSpec classes
     */
    @Override
    public String[] getInteractionSpecsSupported() {
        return new String[] {};
    }

    /**
     * Returns true if the implementation class for the Interaction
     * 
     * @return boolean Depending on method support
     */
    @Override
    public boolean supportsExecuteWithInputAndOutputRecord() {
        return false;
    }

    /**
     * Returns true if the implementation class for the Interaction
     * 
     * @return boolean Depending on method support
     */
    @Override
    public boolean supportsExecuteWithInputRecordOnly() {
        return false;
    }

    /**
     * Returns true if the resource adapter implements the LocalTransaction
     * 
     * @return true If resource adapter supports resource manager local
     *         transaction demarcation
     */
    @Override
    public boolean supportsLocalTransactionDemarcation() {
        return false;
    }

}
