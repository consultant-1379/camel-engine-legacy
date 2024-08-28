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

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;

import org.slf4j.LoggerFactory;

/**
 * Managed connection factory implementation
 * 
 * @author edejket
 * 
 */
public class CamelManagedConnectionFactory implements ManagedConnectionFactory,
        ResourceAdapterAssociation {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1427292001707443738L;

    /** The logger */
    private static final org.slf4j.Logger LOG = LoggerFactory
            .getLogger(CamelManagedConnectionFactory.class);

    /** The resource adapter */
    private ResourceAdapter resourceAdapter;

    /** The logwriter */
    private PrintWriter logWriter;

    /** name */
    @ConfigProperty(defaultValue = "Camel Managed Connection Factory")
    private String name;

    /**
     * Default constructor
     */
    public CamelManagedConnectionFactory() {
        this.resourceAdapter = null;
        this.logWriter = null;
    }

    /**
     * Set name
     * 
     * @param name
     *            The value
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get name
     * 
     * @return The value
     */
    public String getName() {
        return name;
    }

    /**
     * Creates a Connection Factory instance.
     * 
     * @param cxManager
     *            ConnectionManager to be associated with created EIS connection
     *            factory instance
     * @return CamelConnectionFactory
     * @see com.ericsson.oss.mediation.camel.jca.ra.CamelConnectionFactoryImpl
     * @throws ResourceException
     *             Generic exception
     */
    @Override
    public Object createConnectionFactory(final ConnectionManager cxManager)
            throws ResourceException {
        LOG.debug("createConnectionFactory method called...");
        return new CamelConnectionFactoryImpl(this, cxManager);
    }

    /**
     * Creates a Connection Factory instance.
     * 
     * @return EIS-specific Connection Factory instance or
     *         javax.resource.cci.ConnectionFactory instance
     * @throws ResourceException
     *             Generic exception
     */
    @Override
    public Object createConnectionFactory() throws ResourceException {
        throw new ResourceException(
                "This resource adapter doesn't support non-managed environments");
    }

    /**
     * Creates a new physical connection to the underlying EIS resource manager.
     * 
     * @param subject
     *            Caller's security information
     * @param cxRequestInfo
     *            Additional resource adapter specific connection request
     *            information
     * @throws ResourceException
     *             generic exception
     * @return ManagedConnection instance
     */
    @Override
    public ManagedConnection createManagedConnection(final Subject subject,
            final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        LOG.debug(
                "createManagedConnection subject=[{}], connectionRequestInfo=[{}]",
                new Object[] {
                        subject,
                        cxRequestInfo == null ? "null" : cxRequestInfo
                                .toString() });
        return new CamelManagedConnection(this, cxRequestInfo);
    }

    /**
     * Returns a matched connection from the candidate set of connections.
     * 
     * @param connectionSet
     *            Candidate connection set
     * @param subject
     *            Caller's security information
     * @param newRequest
     *            Additional resource adapter specific connection request
     *            information
     * @throws ResourceException
     *             generic exception
     * @return ManagedConnection if resource adapter finds an acceptable match
     *         otherwise null
     */
    @Override
    public ManagedConnection matchManagedConnections(
            @SuppressWarnings("rawtypes") final Set connectionSet,
            final Subject subject, final ConnectionRequestInfo newRequest)
            throws ResourceException {
        LOG.debug(
                "matchManagedConnections called with subject=[{}], connectionRequestInfo=[{}], connectionSet size=[{}]",
                new Object[] { subject,
                        newRequest == null ? "null" : newRequest.toString(),
                        connectionSet.size() });
        ManagedConnection result = null;
        @SuppressWarnings("unchecked")
        final Iterator<ManagedConnection> iter = connectionSet.iterator();
        while (result == null && iter.hasNext()) {
            final ManagedConnection poolConn = iter.next();
            if (poolConn instanceof CamelManagedConnection) {
                result = poolConn;
            }
        }
        LOG.debug("matchManagedConnections returning {}", result);
        return result;
    }

    /**
     * Get the log writer for this ManagedConnectionFactory instance.
     * 
     * @return PrintWriter
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        LOG.debug("getLogWriter()");
        return logWriter;
    }

    /**
     * Set the log writer for this ManagedConnectionFactory instance.
     * 
     * @param out
     *            PrintWriter - an out stream for error logging and tracing
     * @throws ResourceException
     *             generic exception
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws ResourceException {
        LOG.debug("setLogWriter()");
        logWriter = out;
    }

    /**
     * Get the resource adapter
     * 
     * @return The handle
     */
    @Override
    public ResourceAdapter getResourceAdapter() {
        LOG.debug("getResourceAdapter()");
        return resourceAdapter;
    }

    /**
     * Set the resource adapter
     * 
     * @param ra
     *            The handle
     */
    @Override
    public void setResourceAdapter(final ResourceAdapter resourceAdapter) {
        LOG.debug("setResourceAdapter()");
        this.resourceAdapter = resourceAdapter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CamelManagedConnectionFactory other = (CamelManagedConnectionFactory) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
