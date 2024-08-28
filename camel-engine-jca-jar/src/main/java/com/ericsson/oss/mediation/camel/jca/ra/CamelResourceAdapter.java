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

import java.io.Serializable;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CamelResourceAdapter acts as main entry point for RA.
 * 
 * @author edejket
 * 
 */
public class CamelResourceAdapter implements ResourceAdapter, Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -3686723295932165682L;

	/** The logger */
	private static final Logger log = LoggerFactory
			.getLogger(CamelResourceAdapter.class);

	/**
	 * Resource adapter specific settings
	 */
	/** name */
	@ConfigProperty(defaultValue = "Camel Resource Adapter")
	private String name;

	/** version */
	@ConfigProperty(defaultValue = "1.0")
	private String version;

	/**
	 * Pool related settings with default values
	 */
	@ConfigProperty(defaultValue = "125")
	private int poolSize;

	@ConfigProperty(defaultValue = "1")
	private int maxActive;

	@ConfigProperty(defaultValue = "1")
	private int maxIdle;

	@ConfigProperty(defaultValue = "800000")
	private long waitTime;

	@ConfigProperty(defaultValue = "5000")
	private long evictionEligibleAfter;

	@ConfigProperty(defaultValue = "15000")
	private long evictionThreadRunTime;

	@ConfigProperty(defaultValue = "50")
	private int numTestsPerEvictionRun;

	/**
	 * Camel related
	 */
	private CamelContext camelContext;

	/**
	 * JCA workmanaged related
	 */
	private WorkManager workManager;

	/**
	 * This is called when a resource adapter instance is bootstrapped.
	 * 
	 * @param ctx
	 *            A bootstrap context containing references
	 * @throws ResourceAdapterInternalException
	 *             indicates bootstrap failure.
	 */
	@Override
	public void start(final BootstrapContext ctx)
			throws ResourceAdapterInternalException {
		log.info("Starting resorce adapter ...");
		try {
			final PoolConfiguration poolConfig = new PoolConfiguration(
					poolSize, maxActive, maxIdle, waitTime,
					evictionEligibleAfter, evictionThreadRunTime,
					numTestsPerEvictionRun);
			this.workManager = ctx.getWorkManager();
			final CamelContextWorkerThread workerThread = new CamelContextWorkerThread(
					this, poolConfig, true);
			workerThread.run();
			log.info("Started resorce adapter ...");
		} catch (final Exception e) {
			log.error(
					"Exception caught trying to start camel engine, stacktrace {}",
					e);
			throw new ResourceAdapterInternalException(e);
		}
	}

	/**
	 * This is called when a resource adapter instance is undeployed or during
	 * application server shutdown.
	 */
	@Override
	public void stop() {
		try {
			log.info("Stopping resource adapter...");
			final PoolConfiguration poolConfig = new PoolConfiguration(
					poolSize, maxActive, maxIdle, waitTime,
					evictionEligibleAfter, evictionThreadRunTime,
					numTestsPerEvictionRun);
			final CamelContextWorkerThread workerThread = new CamelContextWorkerThread(
					this, poolConfig, false);
			workerThread.run();
			log.info("Resource adapter stopped ...");
		} catch (final Exception e) {
			log.error(
					"Exception caught while trying to stop camel engine, stacktrace {}",
					e);
		}
	}

	/**
	 * This is called during the activation of a message endpoint.
	 * 
	 * @param endpointFactory
	 *            A message endpoint factory instance.
	 * @param spec
	 *            An activation spec JavaBean instance.
	 * @throws ResourceException
	 *             generic exception
	 */
	@Override
	public void endpointActivation(
			final MessageEndpointFactory endpointFactory,
			final ActivationSpec spec) throws ResourceException {
	}

	/**
	 * This is called when a message endpoint is deactivated.
	 * 
	 * @param endpointFactory
	 *            A message endpoint factory instance.
	 * @param spec
	 *            An activation spec JavaBean instance.
	 */
	@Override
	public void endpointDeactivation(
			final MessageEndpointFactory endpointFactory,
			final ActivationSpec spec) {
	}

	/**
	 * This method is called by the application server during crash recovery.
	 * 
	 * @param specs
	 *            An array of ActivationSpec JavaBeans
	 * @throws ResourceException
	 *             generic exception
	 * @return An array of XAResource objects
	 */
	@Override
	public XAResource[] getXAResources(final ActivationSpec[] specs)
			throws ResourceException {
		log.trace("getXAResources()");
		return new XAResource[] {};
	}

	/**
	 * Get camel context
	 * 
	 * @return The value
	 */
	public CamelContext getCamelContext() {
		return this.camelContext;
	}

	/**
	 * @param camelContext
	 *            the camelContext to set
	 */
	void setCamelContext(final CamelContext camelContext) {
		this.camelContext = camelContext;
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
	 * Set version
	 * 
	 * @param version
	 *            The value
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * Get version
	 * 
	 * @return The value
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @return the workManager
	 */
	public WorkManager getWorkManager() {
		return workManager;
	}

	/**
	 * @param workManager
	 *            the workManager to set
	 */
	public void setWorkManager(final WorkManager workManager) {
		this.workManager = workManager;
	}

	/**
	 * @return the poolSize
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * @param poolSize
	 *            the poolSize to set
	 */
	public void setPoolSize(final int poolSize) {
		this.poolSize = poolSize;
	}

	/**
	 * @return the maxActive
	 */
	public int getMaxActive() {
		return maxActive;
	}

	/**
	 * @param maxActive
	 *            the maxActive to set
	 */
	public void setMaxActive(final int maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * @return the maxIdle
	 */
	public int getMaxIdle() {
		return maxIdle;
	}

	/**
	 * @param maxIdle
	 *            the maxIdle to set
	 */
	public void setMaxIdle(final int maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * @return the waitTime
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * @param waitTime
	 *            the waitTime to set
	 */
	public void setWaitTime(final long waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * @return the evictionEligibleAfter
	 */
	public long getEvictionEligibleAfter() {
		return evictionEligibleAfter;
	}

	/**
	 * @param evictionEligibleAfter
	 *            the evictionEligibleAfter to set
	 */
	public void setEvictionEligibleAfter(final long evictionEligibleAfter) {
		this.evictionEligibleAfter = evictionEligibleAfter;
	}

	/**
	 * @return the evictionThreadRunTime
	 */
	public long getEvictionThreadRunTime() {
		return evictionThreadRunTime;
	}

	/**
	 * @param evictionThreadRunTime
	 *            the evictionThreadRunTime to set
	 */
	public void setEvictionThreadRunTime(final long evictionThreadRunTime) {
		this.evictionThreadRunTime = evictionThreadRunTime;
	}

	/**
	 * @return the numTestsPerEvictionRun
	 */
	public final int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	/**
	 * @param numTestsPerEvictionRun
	 *            the numTestsPerEvictionRun to set
	 */
	public final void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
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
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		final CamelResourceAdapter other = (CamelResourceAdapter) obj;
		return equalsCamelResourceAdapter(other);
	}

	/*
	 * used to reduce cyclomatic complexity and clear PMD warnings for equals
	 * method above
	 */
	private boolean equalsCamelResourceAdapter(final CamelResourceAdapter other) {
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!version.equals(other.version)) {
			return false;
		}
		return true;
	}

}
