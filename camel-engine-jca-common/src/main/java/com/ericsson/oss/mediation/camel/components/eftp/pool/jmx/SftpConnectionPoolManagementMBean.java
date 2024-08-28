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
package com.ericsson.oss.mediation.camel.components.eftp.pool.jmx;

public interface SftpConnectionPoolManagementMBean {

    /**
     * Get number of idle objects in the pool
     * 
     * @return Number of idle objects
     */
    int getNumIdle();

    /**
     * Get maximum number of active connections in the pool
     * 
     * @return maxActive
     */
    int getMaxActive();

    /**
     * Set maximum number of active connections in the pool
     * 
     * @param maxActive
     *            maximum number of active connections to set
     */
    void setMaxActive(final int maxActive);

    /**
     * Set maximum number of idle connections
     * 
     * @param maxIdle
     *            maximum number of idle connections
     */
    void setMaxIdle(final int maxIdle);

    /**
     * Get the maximum number of idle connections
     * 
     * @return the maximum number of idle connections
     */
    int getMaxIdle();

    /**
     * Set total number of connections
     * 
     * @param maxTotal
     */
    void setMaxTotal(final int maxTotal);

    /**
     * Get total number of connections
     * 
     * @return total number of connections
     */
    int getMaxTotal();

    /**
     * Set max time in miliseconds that request will wait for object from the
     * pool
     * 
     * @param maxWait
     *            Time to wait in miliseconds
     */
    void setMaxWait(final long maxWait);

    /**
     * Get max time in miliseconds that request will wait for object from the
     * pool
     * 
     * @return max wait time in miliseconds
     */
    long getMaxWait();

    /**
     * Set time in miliseconds for object to be marked as idle
     * 
     * @param minEvictableIdleTimeMillis
     *            time in miliseconds
     */
    void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis);

    /**
     * Get time in miliseconds for object to be marked as idle
     * 
     * @return time in miliseconds
     */
    long getMinEvictableIdleTimeMillis();

    /**
     * Set time between runs of eviction thread
     * 
     * @param timeBetweenEvictionRunsMillis
     *            time in miliseconds
     */
    void setTimeBetweenEvictionRunsMillis(
            final long timeBetweenEvictionRunsMillis);

    /**
     * Get time between runs of eviction thread
     * 
     * @return time in miliseconds
     */
    long getTimeBetweenEvictionRunsMillis();

    /**
     * Clear the pool
     */
    void clearPool();

    /**
     * Get cuurent pool size, this will return number of active objects plus
     * number of idle objects
     * 
     * @return Number of active + idle objects in the pool
     */
    int getCurrentPoolSize();

    /**
     * Clear oldest objects from the pool
     */
    void clearOldest();

    /**
     * Run eviction thread, or stop it if it is already running
     * 
     * @throws Exception
     */
    void evict() throws Exception; // NOPMD by edejket on 3/11/13 8:58 AM

    /**
     * Returns number of active objects in the pool
     * 
     * @return Number of active objects in the pool
     */
    int getNumActive();

    /**
     * Set the number of objects tested for eviction
     * 
     * @param numTestsPerEvictionRun
     */
    void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun);

    /**
     * Get the number of objects tested for eviction
     * 
     * @return number of objects per each test
     */
    int getNumTestsPerEvictionRun();

    /**
     * Test connection on borrow
     * 
     * @param arg
     *            true/false
     */
    void setTestOnBorrow(final boolean arg);

    /**
     * Test connection on return
     * 
     * @param arg
     *            true/false
     */
    void setTestOnReturn(final boolean arg);

    /**
     * Test connection when idle
     * 
     * @param arg
     *            true/false
     */
    void setTestWhileIdle(final boolean arg);

    /**
     * Getters
     * 
     */
    boolean getTestOnBorrow();

    boolean getTestOnReturn();

    boolean getTestWhileIdle();

}
