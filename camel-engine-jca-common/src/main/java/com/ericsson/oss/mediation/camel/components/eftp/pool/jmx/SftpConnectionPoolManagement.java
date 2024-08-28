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

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import com.ericsson.oss.mediation.camel.components.eftp.pool.ConnectionConfig;

public class SftpConnectionPoolManagement implements
        SftpConnectionPoolManagementMBean {

    /** Registered */
    private boolean registered;

    private GenericKeyedObjectPool<ConnectionConfig, ?> objectPool;

    /**
     * Default constructor
     */
    public SftpConnectionPoolManagement() {
        super();
    }

    /**
     * Full constructor
     * 
     * @param objectPool
     * @param objectName
     * @param mbeanServer
     */
    public SftpConnectionPoolManagement(
            final GenericKeyedObjectPool<ConnectionConfig, ?> objectPool) {
        this.objectPool = objectPool;
    }

    @Override
    public int getNumIdle() {
        return this.objectPool.getNumIdle();
    }

    @Override
    public int getNumActive() {
        return this.objectPool.getNumActive();
    }

    @Override
    public int getMaxActive() {
        return this.objectPool.getMaxActive();
    }

    @Override
    public void setMaxActive(final int maxActive) {
        this.objectPool.setMaxActive(maxActive);
    }

    @Override
    public void setMaxIdle(final int maxIdle) {
        this.objectPool.setMaxIdle(maxIdle);
    }

    @Override
    public int getMaxIdle() {
        return this.objectPool.getMaxIdle();
    }

    @Override
    public void setMaxTotal(final int maxTotal) {
        this.objectPool.setMaxTotal(maxTotal);
    }

    @Override
    public int getMaxTotal() {
        return this.objectPool.getMaxTotal();
    }

    @Override
    public void setMaxWait(final long maxWait) {
        this.objectPool.setMaxWait(maxWait);
    }

    @Override
    public long getMaxWait() {
        return this.objectPool.getMaxWait();
    }

    @Override
    public void setMinEvictableIdleTimeMillis(
            final long minEvictableIdleTimeMillis) {
        this.objectPool
                .setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @Override
    public long getMinEvictableIdleTimeMillis() {
        return this.objectPool.getMinEvictableIdleTimeMillis();
    }

    @Override
    public void setTimeBetweenEvictionRunsMillis(
            final long timeBetweenEvictionRunsMillis) {
        this.objectPool
                .setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    @Override
    public long getTimeBetweenEvictionRunsMillis() {
        return this.objectPool.getTimeBetweenEvictionRunsMillis();
    }

    @Override
    public void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun) {
        this.objectPool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    @Override
    public int getNumTestsPerEvictionRun() {
        return this.objectPool.getNumTestsPerEvictionRun();
    }

    @Override
    public void clearPool() {
        this.objectPool.clear();
    }

    @Override
    public int getCurrentPoolSize() {
        return this.objectPool.getNumActive() + this.objectPool.getNumIdle();
    }

    @Override
    public void clearOldest() {
        this.objectPool.clearOldest();
    }

    @Override
    public void evict() throws Exception { // NOPMD by edejket on 3/11/13 8:57 AM
        this.objectPool.evict();
    }

    /**
     * @return the registered
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * @param registered
     *            the registered to set
     */
    public void setRegistered(final boolean registered) {
        this.registered = registered;
    }

    /**
     * @return the objectPool
     */
    public GenericKeyedObjectPool<ConnectionConfig, ?> getObjectPool() {
        return objectPool;
    }

    /**
     * @param objectPool
     *            the objectPool to set
     */
    public void setObjectPool(
            final GenericKeyedObjectPool<ConnectionConfig, ?> objectPool) {
        this.objectPool = objectPool;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.
     * SftpConnectionPoolManagementMBean#setTestOnBorrow(boolean)
     */
    @Override
    public void setTestOnBorrow(final boolean arg) {
        this.objectPool.setTestOnBorrow(arg);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.
     * SftpConnectionPoolManagementMBean#setTestOnReturn(boolean)
     */
    @Override
    public void setTestOnReturn(final boolean arg) {
        this.objectPool.setTestOnReturn(arg);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.
     * SftpConnectionPoolManagementMBean#setTestWhileIdle(boolean)
     */
    @Override
    public void setTestWhileIdle(final boolean arg) {
        this.objectPool.setTestWhileIdle(arg);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.
     * SftpConnectionPoolManagementMBean#getTestOnBorrow()
     */
    @Override
    public boolean getTestOnBorrow() {
        return this.objectPool.getTestOnBorrow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.
     * SftpConnectionPoolManagementMBean#getTestOnReturn()
     */
    @Override
    public boolean getTestOnReturn() {
        return this.objectPool.getTestOnReturn();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.
     * SftpConnectionPoolManagementMBean#getTestWhileIdle()
     */
    @Override
    public boolean getTestWhileIdle() {
        return this.objectPool.getTestWhileIdle();
    }

}
