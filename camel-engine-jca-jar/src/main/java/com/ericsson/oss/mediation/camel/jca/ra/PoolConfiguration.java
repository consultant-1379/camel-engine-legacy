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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolConfiguration implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6760441197966050275L;
    private static final Logger log = LoggerFactory
            .getLogger(PoolConfiguration.class);

    /**
     * Pool related settings with default values
     */
    private int poolSize;

    private int maxActive;

    private int maxIdle;

    private long waitTime;

    private long evictionEligibleAfter;

    private long evictionThreadRunTime;

    private int numTestsPerEvictionRun;

    /**
     * @param poolSize
     * @param maxActive
     * @param maxIdle
     * @param waitTime
     * @param evictionEligibleAfter
     * @param evictionThreadRunTime
     */
    public PoolConfiguration(final int poolSize, final int maxActive,
            final int maxIdle, final long waitTime,
            final long evictionEligibleAfter, final long evictionThreadRunTime,
            final int numTestsPerEvictionRun) {
        super();
        log.info(
                "Creating pool configuration with poolSize={}, maxActive={},maxIdle={}, waitTime={}, evictionEligibleAfter={}, evictionThreadRunTime={}",
                new Object[] { poolSize, maxActive, maxIdle, waitTime,
                        evictionEligibleAfter, evictionThreadRunTime });
        this.poolSize = poolSize;
        this.maxActive = maxActive;
        this.maxIdle = maxIdle;
        this.waitTime = waitTime;
        this.evictionEligibleAfter = evictionEligibleAfter;
        this.evictionThreadRunTime = evictionThreadRunTime;
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
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
     * Return maximum number of instances in the pool for this key
     * 
     * @return the maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * Set maximum number of instances in the pool for this key
     * 
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

}
