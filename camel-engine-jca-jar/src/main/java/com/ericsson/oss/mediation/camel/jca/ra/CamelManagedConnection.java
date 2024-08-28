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
import java.util.*;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnection;

/**
 * JCA Generated class, represents managed connection holding a handle for
 * physical connection to EIS
 * 
 * @author edejket
 * 
 */
public class CamelManagedConnection implements ManagedConnection {

    /** The logger */
    private static final Logger LOG = LoggerFactory
            .getLogger(CamelManagedConnection.class);

    /** The logwriter */
    private PrintWriter logWriter = null;

    /** ManagedConnectionFactory */
    private CamelManagedConnectionFactory managedConnectionFactory = null;

    /** Listeners */
    private List<ConnectionEventListener> listeners;

    /** Connection */
    private Object connection = null;

    /** MetaData information */
    private CamelManagedConnectionMetaData metaData;

    /** RequestInformation used for this managed connection */
    private CamelConnectionRequestInfo requestInfo;

    /**
     * Default constructor for creating managed connection, in case of pooling
     * they will be created if prefill-pool is specified, and in that case
     * cxRequestInfo will be null. If we are creating new connection, since none
     * from pool are able to satisfy our request, cxRequestInfo should not be
     * null.
     * 
     * @param mcf
     *            ManagedConnectionFactory mcf
     * @param cxRequestInfo
     *            ConnectionRequestInformation, can be null if we are creating
     *            empty connection to fill the pool
     */
    public CamelManagedConnection(final CamelManagedConnectionFactory mcf,
            final ConnectionRequestInfo cxRequestInfo) {
        final CamelConnectionRequestInfo reqInfo = (CamelConnectionRequestInfo) cxRequestInfo;
        LOG.debug("CamelManagedConnection constructor called...");
        this.managedConnectionFactory = mcf;
        this.requestInfo = reqInfo;
        this.listeners = Collections
                .synchronizedList(new ArrayList<ConnectionEventListener>(1));
        this.connection = null;
        this.metaData = new CamelManagedConnectionMetaData();

    }

    /**
     * Apply input on the route
     * 
     * @param routeId
     *            Id of the route ( in terms of camel context )
     * @param request
     *            MediationTaskRequest containing
     * @throws ResourceException
     */
    public void applyInput(final String routeId,
            final Map<String, Object> headers) throws ResourceException {
        LOG.debug("applyInfo method called");
        final Map<String, Object> headersID = resolveHeadersID(headers);
        final CamelResourceAdapter ra = (CamelResourceAdapter) this
                .getManagedConnectionFactory().getResourceAdapter();
        final CamelContext camelCtx = ra.getCamelContext();
        final CamelEngineWorkerThread worker = new CamelEngineWorkerThread(camelCtx,
                headersID, routeId);
        ra.getWorkManager().startWork(worker, 5000, null, null);

    }

    /**
     * @param headers
     * @return
     */
    Map<String, Object> resolveHeadersID(final Map<String, Object> headers) {
        final Map<String, Object> headersID = new HashMap<>();
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            final String resolvedKey = removeKeyPrefix(entry.getKey());
            headersID.put(resolvedKey, entry.getValue());
        }
        return headersID;
    }

    /**
     * @param key
     * @return
     */
    private String removeKeyPrefix(final String key) {
        String result = "";
        LOG.debug("Resolving headre id: {}", key);
        result = key.substring(key.lastIndexOf("|") + 1, key.length());
        return result;
    }

    /**
     * Creates a new connection handle for the underlying physical connection
     * represented by the ManagedConnection instance.
     * 
     * @param subject
     *            Security context as JAAS subject
     * @param cxRequestInfo
     *            ConnectionRequestInfo instance
     * @return generic Object instance representing the connection handle.
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public Object getConnection(final Subject subject,
            final ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        LOG.debug(
                "getConnection(Subject subject,ConnectionRequestInfo cxRequestInfo) called with {},{}",
                new Object[] { subject, cxRequestInfo });
        if (cxRequestInfo == null) {
            throw new ResourceException(
                    "Unable to create connection without connection request information");
        }

        this.requestInfo = (CamelConnectionRequestInfo) cxRequestInfo;
        final CamelResourceAdapter camelRA = (CamelResourceAdapter) this.managedConnectionFactory
                .getResourceAdapter();
        final List<DataPathDefinition> pathDefs = this.requestInfo.getPathDef();

        /**
         * Was this route already created?
         */
        final DataPathDefinition mainPath = pathDefs.get(0);
        final List<DataPathDefinition> reverse = this
                .createReversOrderList(pathDefs);
        for (DataPathDefinition path : reverse) {
            final String routeId = path.getId();
            final CamelContext camelCtx = camelRA.getCamelContext();
            synchronized (camelCtx) {
                if (camelCtx.getRoute(routeId) == null) {
                    LOG.debug(
                            "Route {} doesnt exists, creating new camel route.",
                            routeId);
                    final ClassLoader threadCtxClassLoader = Thread
                            .currentThread().getContextClassLoader();
                    try {
                        /**
                         * Switch thread context class loader
                         * 
                         */
                        Thread.currentThread().setContextClassLoader(// NOPMD by edejket on 4/16/13 8:45 AM
                                camelRA.getClass().getClassLoader());// NOPMD by edejket on 4/16/13 8:45 AM
                        final CamelRouteBuilder routeBuilder = new CamelRouteBuilder(
                                path);
                        camelCtx.addRoutes(routeBuilder);
                        LOG.debug("Added new route with id {}", routeId);
                        final CamelConnectionImpl camelConnection = new CamelConnectionImpl(
                                this, routeId);
                        // associate with connection
                        if (mainPath.getId().equals(
                                camelConnection.getRouteId())) {
                            this.connection = camelConnection;
                        }
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                        throw new ResourceException(e.getMessage());
                    } finally {
                        Thread.currentThread().setContextClassLoader(
                                threadCtxClassLoader);
                    }

                } else {
                    // there is already created route with such name
                    LOG.debug(
                            "Route {} exists, returning existing camel route",
                            routeId);
                    final CamelConnectionImpl camelConnection = new CamelConnectionImpl(
                            this, routeId);
                    // associate with connection
                    if (mainPath.getId().equals(camelConnection.getRouteId())) {
                        this.connection = camelConnection;
                    }
                }
            }
        }
        return this.connection;
    }

    /**
     * @param pathDefs
     * @return
     */
    private List<DataPathDefinition> createReversOrderList(
            final List<DataPathDefinition> pathDefs) {
        final List<DataPathDefinition> reverse = new LinkedList<>();
        final ListIterator<DataPathDefinition> li = pathDefs
                .listIterator(pathDefs.size());
        while (li.hasPrevious()) {
            final DataPathDefinition curr = li.previous();
            reverse.add(curr);
            LOG.debug("Adding to reverse list is: " + curr.getId());
        }
        return reverse;
    }

    /**
     * Used by the container to change the association of an application-level
     * connection handle with a ManagedConneciton instance.
     * 
     * @param connection
     *            Application-level connection handle
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void associateConnection(final Object connection)
            throws ResourceException {
        LOG.debug("associateConnection()");
        if (connection == null) {
            throw new ResourceException(
                    "Attempting to associate null connection handle to managed connection");
        }
        if (!(connection instanceof EngineConnection)) {
            throw new ResourceException(
                    "Wrong connection handle type, connection handle is not instance of CamelConnection");
        }
        this.connection = connection;
    }

    /**
     * Application server calls this method to force any cleanup on the
     * ManagedConnection instance.
     * 
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void cleanup() throws ResourceException

    {
        // cleanup of all client specific state, before connection is returned
        // to pool
        LOG.debug("cleanup() method called");
        this.requestInfo = null;
        this.metaData = null;
        this.connection = null;
    }

    /**
     * Destroys the physical connection to the underlying resource manager.
     * 
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public void destroy() throws ResourceException {
        LOG.debug("destroy()");
        this.requestInfo = null;
        this.metaData = null;
        this.connection = null;
    }

    /**
     * Adds a connection event listener to the ManagedConnection instance.
     * 
     * @param listener
     *            A new ConnectionEventListener to be registered
     */
    @Override
    public void addConnectionEventListener(
            final ConnectionEventListener listener) {
        LOG.debug("addConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }
        this.listeners.add(listener);
    }

    /**
     * Removes an already registered connection event listener from the
     * ManagedConnection instance.
     * 
     * @param listener
     *            already registered connection event listener to be removed
     */
    @Override
    public void removeConnectionEventListener(
            final ConnectionEventListener listener) {
        LOG.debug("removeConnectionEventListener()");
        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }
        this.listeners.remove(listener);
    }

    /**
     * Close handle
     * 
     * @param handle
     *            The handle
     */
    public void closeHandle(final EngineConnection handle) {
        final ConnectionEvent event = new ConnectionEvent(this,
                ConnectionEvent.CONNECTION_CLOSED);
        LOG.debug("closeHandle has been called...");
        event.setConnectionHandle(handle);
        for (final ConnectionEventListener cel : this.listeners) {
            LOG.debug("cel.connectionClosed(event)");
            cel.connectionClosed(event);
        }
        LOG.debug("closeHandle method complete");

    }

    /**
     * Gets the log writer for this ManagedConnection instance.
     * 
     * @return Character ourput stream associated with this Managed-Connection
     *         instance
     */
    @Override
    public PrintWriter getLogWriter() {
        LOG.debug("getLogWriter()");
        return this.logWriter;
    }

    /**
     * Sets the log writer for this ManagedConnection instance.
     * 
     * @param out
     *            Character Output stream to be associated
     */
    @Override
    public void setLogWriter(final PrintWriter out) {
        LOG.debug("setLogWriter()");
        this.logWriter = out;
    }

    /**
     * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.
     * 
     * @return LocalTransaction instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("LocalTransaction not supported");
    }

    /**
     * Returns an <code>javax.transaction.xa.XAresource</code> instance.
     * 
     * @return XAResource instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException(
                "GetXAResource not supported not supported");
    }

    /**
     * Gets the metadata information for this connection's underlying EIS
     * resource manager instance.
     * 
     * @return ManagedConnectionMetaData instance
     * @throws ResourceException
     *             generic exception if operation fails
     */
    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        LOG.debug("getMetaData()");
        return this.metaData;
    }

    /**
     * @return the connection
     */
    Object getConnection() {
        return this.connection;
    }

    /**
     * @param connection
     *            the connection to set
     */
    void setConnection(final Object connection) {
        this.connection = connection;
    }

    /**
     * @param listeners
     *            the listeners to set
     */
    void setListeners(final List<ConnectionEventListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * @return the requestInfo
     */
    CamelConnectionRequestInfo getRequestInfo() {
        return this.requestInfo;
    }

    /**
     * @param requestInfo
     *            the requestInfo to set
     */
    void setRequestInfo(final CamelConnectionRequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    /**
     * @return the managedConnectionFactory
     */
    CamelManagedConnectionFactory getManagedConnectionFactory() {
        return this.managedConnectionFactory;
    }

    /**
     * @param managedConnectionFactory
     *            the managedConnectionFactory to set
     */
    void setManagedConnectionFactory(
            final CamelManagedConnectionFactory managedConnectionFactory) {
        this.managedConnectionFactory = managedConnectionFactory;
    }

}
