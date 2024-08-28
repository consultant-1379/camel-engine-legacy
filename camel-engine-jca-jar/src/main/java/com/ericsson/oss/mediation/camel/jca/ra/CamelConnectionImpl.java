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

import java.util.Map;

import javax.resource.ResourceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnection;

/**
 * Implementation of CamelConnection interface, represents single JCA connection
 * to Camel
 * 
 * @author edejket
 * 
 */
public class CamelConnectionImpl implements EngineConnection {

    /**
     * Inside RAR, no CDI
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(CamelConnectionImpl.class);
    private final CamelManagedConnection cmc;
    private String routeId;

    public CamelConnectionImpl(final CamelManagedConnection cmc,
            final String routeId) {
        LOG.debug("CamelConnectionImpl constructor called");
        this.cmc = cmc;
        this.routeId = routeId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ericsson.oss.mediation.mi.jca.camel.api.CamelConnection#applyInput()
     */
    @Override
    public void applyInput(final Map<String, Object> headers)
            throws ResourceException {

        LOG.debug("applyInput method called...");
        this.cmc.applyInput(this.routeId, headers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.mi.jca.camel.api.CamelConnection#close()
     */
    @Override
    public void close() {
        LOG.debug("close method called...");
        this.routeId = null;
        this.cmc.closeHandle(this);
    }

    /**
     * Getter method provided for junit testing
     * 
     * @return the routeId
     */
    String getRouteId() {
        return this.routeId;
    }

    /**
     * Setter method provided for junit testing
     * 
     * @param routeId
     *            the routeId to set
     */
    void setRouteId(final String routeId) {
        this.routeId = routeId;
    }

}
