/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2013 
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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.components.eftp.pool.jmx.FtpConnectionPoolManagement;

/**
 * Singleton connection pool for use with the EftpProducer. This class pools
 * FTPClient connections only.
 * 
 * @author etonayr
 * 
 */
public  class FtpConnectionPool {

    private static final Logger LOG = LoggerFactory
            .getLogger(FtpConnectionPool.class);

    private static  GenericKeyedObjectPool<ConnectionConfig, FTPClient> pool;

    public FtpConnectionPool(final int maxActive, final int maxIdle,
            final int maxTotal, final long maxWait,
            final long minEvictableIdleTimeMillis,
            final long timeBetweenEvictionRunsMillis,
            final int numTestsPerEvictionRun) {
        LOG.debug("Initialising FTP connection pool");
        pool = new GenericKeyedObjectPool<ConnectionConfig, FTPClient>(
                FtpPooledConnectionFactory.getInstance(), maxActive,
                GenericKeyedObjectPool.WHEN_EXHAUSTED_BLOCK, maxWait);
        pool.setMaxActive(maxActive);
        pool.setMaxIdle(maxIdle);
        pool.setMaxTotal(maxTotal);
        pool.setMaxWait(maxWait);
        pool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        pool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        pool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        pool.setTestOnBorrow(true);
        pool.setTestOnReturn(false);
        pool.setTestWhileIdle(false);

        setupJmxAgent(pool);
    }

    /**
     * Set up JMX bean used to control the pool
     */
    private static void setupJmxAgent(
            final GenericKeyedObjectPool<ConnectionConfig, FTPClient> pool) {

        try {
            final MBeanServer platformMBeanServer = ManagementFactory
                    .getPlatformMBeanServer();
            final FtpConnectionPoolManagement mbean = new FtpConnectionPoolManagement(
                    pool);
            final ObjectName poolObj = new ObjectName(
                    "com.ericsson.oss.mediation.camel.components.eftp.pool.jmx"
                            + ":type=FtpConnectionPoolManagement");
            if (platformMBeanServer.isRegistered(poolObj)) {
                platformMBeanServer.unregisterMBean(poolObj);
            }
            platformMBeanServer.registerMBean(mbean, poolObj);

        } catch (final Exception e) {
            throw new IllegalStateException(
                    "Problem during registration of monitoring into JMX:" + e);
        }

    }
    
    /**
     * Invalidates the object under this key, object will be destroyed instead of being returned into pool
     */
    public void invalidateObject(final ConnectionConfig key, final FTPClient ftpClient) throws Exception // NOPMD by edejket on 4/16/13 8:45 AM
    {
    	pool.invalidateObject(key, ftpClient);
    }
    
    /**
     * Borrow object from the pool
     */
    public FTPClient borrowObject(final ConnectionConfig key) throws Exception // NOPMD by edejket on 4/16/13 8:45 AM
    {
    	return pool.borrowObject(key);
    }
    
    /**
     * Return object back to pool
     */
    public void returnObject(final ConnectionConfig key,final FTPClient ftpClient) throws Exception // NOPMD by edejket on 4/16/13 8:45 AM
    {
    	pool.returnObject(key, ftpClient);
    }

    

}
