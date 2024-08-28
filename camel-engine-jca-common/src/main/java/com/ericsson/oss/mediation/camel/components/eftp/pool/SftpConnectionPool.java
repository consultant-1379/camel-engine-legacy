/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.mediation.camel.components.eftp.pool;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.SftpConnectionPoolManagement;
import com.jcraft.jsch.*;

public  class SftpConnectionPool {

    private  static GenericKeyedObjectPool<ConnectionConfig, ChannelSftp> pool;

    /**
     * Constructor for SFTP pool
     * 
     * @param maxActive
     *            max number of connections for any given key ( nodes can
     *            support up to 5 )
     * @param maxIdle
     *            max number of idle connections in the pool
     * @param maxTotal
     *            total pool size for all the pools inside (max number of keys
     *            times max number of objects in the pool with that key)
     * @param maxWait
     *            how long should we wait for connection, in ms
     * @param minEvictableIdleTimeMillis
     *            Instance can be evicted if idle in the pool for at least this
     *            long
     * @param timeBetweenEvictionRunsMillis
     *            How often do we run eviction thread
     * @param numTestsPerEvictionRun
     *            Number of test that we will perform per eviction run
     */
    public SftpConnectionPool(final int maxActive, final int maxIdle,
            final int maxTotal, final long maxWait,
            final long minEvictableIdleTimeMillis,
            final long timeBetweenEvictionRunsMillis,
            final int numTestsPerEvictionRun) {

        /**
         * Here maxActive represents maximum number of objects that can be
         * borrowed from this pool at one time For example if pool size is 500,
         * means we have 500 different subpools for any give
         */
        pool = new GenericKeyedObjectPool<ConnectionConfig, ChannelSftp>(
                SftpPooledConnectionFactory.getInstance(), maxActive,
                GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK, maxWait);
        pool.setMaxActive(maxActive);
        pool.setMaxIdle(maxIdle);
        pool.setMaxTotal(maxTotal);
        pool.setMaxWait(maxWait);
        pool.setWhenExhaustedAction(GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK);
        pool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        pool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        pool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        pool.setTestOnBorrow(true);
        pool.setTestOnReturn(false);
        pool.setTestWhileIdle(false);

        registerJmxBean(pool);
    }

    private static void registerJmxBean(
            final GenericKeyedObjectPool<ConnectionConfig, ChannelSftp> cpool) {
        try {
            final MBeanServer platformMBeanServer = ManagementFactory
                    .getPlatformMBeanServer();
            final SftpConnectionPoolManagement mbean = new SftpConnectionPoolManagement(
                    cpool);
            final ObjectName poolObj = new ObjectName(
                    "com.ericsson.oss.mediation.camel.components.eftp.pool.jmx"
                            + ":type=SftpConnectionPoolManagement");
            if (platformMBeanServer.isRegistered(poolObj)) {
                platformMBeanServer.unregisterMBean(poolObj);
            }
            platformMBeanServer.registerMBean(mbean, poolObj);

        } catch (final Exception t) {
            throw new IllegalStateException(
                    "Problem during registration of monitoring into JMX:" + t);
        }

    }
    
    public void invalidateObject(final ConnectionConfig key, final ChannelSftp channel) throws Exception // NOPMD by edejket on 4/16/13 8:45 AM
    {
    	pool.invalidateObject(key, channel);
    }
    
    public ChannelSftp borrowObject(final ConnectionConfig key) throws Exception // NOPMD by edejket on 4/16/13 8:45 AM
    {
    	return pool.borrowObject(key);
    }
    
    public void returnObject(final ConnectionConfig key,final ChannelSftp channel) throws Exception // NOPMD by edejket on 4/16/13 8:45 AM
    {
    	pool.returnObject(key, channel);
    }

    

}
