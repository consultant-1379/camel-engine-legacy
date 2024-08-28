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

import java.util.List;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnection;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnectionFactory;

/**
 * JCA Connection factory implementation for CamelConnections
 * 
 * @author edejket
 * 
 */
public class CamelConnectionFactoryImpl implements EngineConnectionFactory {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5510634514071332731L;
    /**
     * Since we are now inside RAR, we no longer have CDI, so we have to use
     * slf4j logger
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(CamelConnectionFactoryImpl.class);

    private Reference reference;
    /**
     * Reference to our managed connection factory
     */
    private final CamelManagedConnectionFactory cmcf;
    /**
     * Reference to connection manager, implemented and provided to us by AS
     */
    private final ConnectionManager connectionManager;

    /**
     * Constructor
     * 
     * @param cmcf
     *            ManagedConnectionFactory
     * @param connectionManager
     *            ConnectionManager from AS
     */
    public CamelConnectionFactoryImpl(final CamelManagedConnectionFactory cmcf,
            final ConnectionManager connectionManager) {
        this.cmcf = cmcf;
        this.connectionManager = connectionManager;
    }

    @Override
    public EngineConnection getConnection(final List<DataPathDefinition> pathDef)
            throws ResourceException {

        LOG.debug(
                "getConnection method called with dataPathDefinition id=[{}]",
                pathDef.get(0).getId());
        final CamelConnectionRequestInfo reqInfo = new CamelConnectionRequestInfo(
                pathDef);
        return (EngineConnection) this.connectionManager.allocateConnection(
                this.cmcf, reqInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.Referenceable#setReference(javax.naming.Reference)
     */
    @Override
    public void setReference(final Reference reference) {
        this.reference = reference;

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Referenceable#getReference()
     */
    @Override
    public Reference getReference() throws NamingException {
        return this.reference;
    }

    /**
     * @return the cmcf
     */
    CamelManagedConnectionFactory getCmcf() {
        return this.cmcf;
    }

    /**
     * @return the connectionManager
     */
    ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }

}
