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

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.components.eftp.pool.*;
import com.jcraft.jsch.Session;

public class CamelContextWorkerThread {

    private final PoolConfiguration poolConfig;

    private final boolean startup;

    private CamelResourceAdapter ra;

    private static final Logger log = LoggerFactory
            .getLogger(CamelContextWorkerThread.class);

    public CamelContextWorkerThread(final CamelResourceAdapter ra,
            final PoolConfiguration config, final boolean startup) {
        this.poolConfig = config;
        this.startup = startup;
        this.ra = ra;
    }

    public void run() {
        if (startup) {
            startEngine();
        } else {
            stopEngine();
        }

    }

    private void startEngine() {
        JndiContext jctx = null;
        try {
            log.debug("Starting camel context ...");
            log.info(
                    "Creating FTP pool with defaults: [poolSize={}, maxIdle={}, maxActive={}, waitTime={}, evictionEligibleAfter={}, evictionThreadRunTime={}",
                    new Object[] { poolConfig.getPoolSize(),
                            poolConfig.getMaxIdle(), poolConfig.getMaxActive(),
                            poolConfig.getWaitTime(),
                            poolConfig.getEvictionEligibleAfter(),
                            poolConfig.getEvictionThreadRunTime() });
            final FtpConnectionPool ftpPool = new FtpConnectionPool(
                    poolConfig.getMaxActive(), poolConfig.getMaxIdle(),
                    poolConfig.getPoolSize(), poolConfig.getWaitTime(),
                    poolConfig.getEvictionEligibleAfter(),
                    poolConfig.getEvictionThreadRunTime(),
                    poolConfig.getNumTestsPerEvictionRun());

            log.info(
                    "Creating SFTP pool with defaults: [poolSize={}, maxIdle={}, maxActive={}, waitTime={}, evictionEligibleAfter={}, evictionThreadRunTime={}",
                    new Object[] { poolConfig.getPoolSize(),
                            poolConfig.getMaxIdle(), poolConfig.getMaxActive(),
                            poolConfig.getWaitTime(),
                            poolConfig.getEvictionEligibleAfter(),
                            poolConfig.getEvictionThreadRunTime() });

            final SftpConnectionPool sftpPool = new SftpConnectionPool(
                    poolConfig.getMaxActive(), poolConfig.getMaxIdle(),
                    poolConfig.getPoolSize(), poolConfig.getWaitTime(),
                    poolConfig.getEvictionEligibleAfter(),
                    poolConfig.getEvictionThreadRunTime(),
                    poolConfig.getNumTestsPerEvictionRun());

            jctx = new JndiContext();

            jctx.bind(Constants.FTP_POOL, ftpPool);
            jctx.bind(Constants.SFTP_POOL, sftpPool);
            final CamelContext camelContext = new DefaultCamelContext(jctx);
            camelContext.start();
            this.ra.setCamelContext(camelContext);
        } catch (Exception e) {
            log.error("Error starting camel context {}", e);
        }
    }

    private void stopEngine() {

        try {
            log.debug("Stopping camel context ...");
            @SuppressWarnings("unchecked")
            final GenericKeyedObjectPool<ConnectionConfig, FTPClient> ftpPool = (GenericKeyedObjectPool<ConnectionConfig, FTPClient>) this.ra
                    .getCamelContext().getRegistry().lookup(Constants.FTP_POOL);

            @SuppressWarnings("unchecked")
            final GenericKeyedObjectPool<ConnectionConfig, Session> sftpPool = (GenericKeyedObjectPool<ConnectionConfig, Session>) this.ra
                    .getCamelContext().getRegistry().lookup(Constants.SFTP_POOL);
            ftpPool.close();
            sftpPool.close();
            this.ra.getCamelContext().stop();
        } catch (Exception e) {
            log.error("Error stopping camel context {}", e);
        }
    }

    public void release() {
        try {
            this.ra = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the ra
     */
    public CamelResourceAdapter getRa() {
        return ra;
    }

    /**
     * @param ra
     *            the ra to set
     */
    public void setRa(final CamelResourceAdapter ra) {
        this.ra = ra;
    }

}
