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

import java.util.logging.Logger;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

/**
 * CamelManagedConnectionMetaData
 * 
 * @version $Revision: $
 */
public class CamelManagedConnectionMetaData implements
        ManagedConnectionMetaData {
    /** The logger */
    private static final Logger LOG = Logger
            .getLogger("CamelManagedConnectionMetaData");

    /**
     * Returns Product name of the underlying EIS instance connected through the
     * ManagedConnection.
     * 
     * @return Product name of the EIS instance
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getEISProductName() throws ResourceException {
        LOG.finest("getEISProductName()");
        return null;
    }

    /**
     * Returns Product version of the underlying EIS instance connected through
     * the ManagedConnection.
     * 
     * @return Product version of the EIS instance
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getEISProductVersion() throws ResourceException {
        LOG.finest("getEISProductVersion()");
        return null; // TODO
    }

    /**
     * Returns maximum limit on number of active concurrent connections
     * 
     * @return Maximum limit for number of active concurrent connections
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public int getMaxConnections() throws ResourceException {
        LOG.finest("getMaxConnections()");
        return 1;
    }

    /**
     * Returns name of the user associated with the ManagedConnection instance
     * 
     * @return Name of the user
     * @throws ResourceException
     *             Thrown if an error occurs
     */
    @Override
    public String getUserName() throws ResourceException {
        LOG.finest("getUserName()");
        return null;
    }

}
