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
import javax.resource.spi.work.Work;

import org.apache.camel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of <code>javax.resource.spi.work.Work</code>, to be submitted
 * to WorkManager. All Camel related code will run from within this thread -
 * taken from JCA pool - long running threads pool.
 * 
 * @author edejket
 * 
 */
public class CamelEngineWorkerThread implements Work {

    private CamelContext camelCtx;
    private Map<String, Object> headers;
    private String routeId;

    private static final Logger log = LoggerFactory
            .getLogger(CamelEngineWorkerThread.class);

    /**
     * Full constructor
     * 
     * @param ra
     *            ResourceAdapter reference
     * @param headers
     *            Headers of message
     * 
     * @param routeId
     *            Id of route on which this exchenge will take place
     */
    public CamelEngineWorkerThread(final CamelContext camelCtx,
            final Map<String, Object> headers, final String routeId) {
        super();
        this.camelCtx = camelCtx;
        this.headers = headers;
        this.routeId = routeId;
    }

    /**
     * Starts camel route
     * 
     * @param routeId
     *            Id of route in camel context
     * @throws ResourceException
     */
    private synchronized void startRoute(final String routeId)
            throws ResourceException {
        try {
            log.trace("Starting route with routeId {}", routeId);
            this.camelCtx.startRoute(routeId);
            log.trace("Started route with routeId {}", routeId);
        } catch (final Exception e) {
            log.error("Exception caught while trying to start route:", e);
            throw new ResourceException(e);
        }

    }

    private synchronized boolean isRouteStarted() {
        final ServiceStatus routeStatus = camelCtx.getRouteStatus(routeId);
        if (routeStatus == null || !routeStatus.isStarted()) {
            log.trace("Route {} is not started...", routeId);
            return false;
        } else {
            return true;
        }

    }

    private void applyInput(final String routeId,
            final Map<String, Object> headers) throws ResourceException {
        log.trace("Apply input called for route=[{}] with headers {}",
                new Object[] { routeId, headers });
        if (!isRouteStarted()) {
            startRoute(routeId);
        }
        try {
            final Route route = camelCtx.getRoute(routeId);
            final Endpoint endpoint = route.getEndpoint();
            final Producer producer = endpoint.createProducer();
            final Exchange ex = endpoint.createExchange();
            ex.getIn().setHeaders(headers);
            producer.process(ex);
            log.trace("applyInfo method invocation ended...");
        } catch (Exception e) {
            throw new ResourceException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        log.trace("run method called for routeId {}", this.routeId);
        try {
            applyInput(routeId, headers);
        } catch (Exception e) {
            log.error("Exception caught during run method call, exception:", e);

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.work.Work#release()
     */
    @Override
    public void release() {
        log.trace("release method invoked for routeId {}", this.routeId);
        this.camelCtx = null;
        if (this.headers != null) {
            this.headers.clear();
        }
        this.headers = null;
        this.routeId = null;
    }

}
