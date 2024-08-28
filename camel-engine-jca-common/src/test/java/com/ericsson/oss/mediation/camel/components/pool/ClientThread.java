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
package com.ericsson.oss.mediation.camel.components.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.components.eftp.pool.ConnectionConfig;
import com.ericsson.oss.mediation.camel.components.eftp.pool.SftpConnectionPool;
import com.jcraft.jsch.ChannelSftp;

@SuppressWarnings("PMD.DoNotUseThreads")
public class ClientThread implements Runnable {

    private static final Logger log = LoggerFactory
            .getLogger(ClientThread.class);

    private final SftpConnectionPool pool;
    private final ConnectionConfig key;
    private final String count;

    public ClientThread(final SftpConnectionPool sftpPool,
            final ConnectionConfig key, final String count) {

        this.pool = sftpPool;
        this.key = key;
        this.count = count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            final ChannelSftp channel1 = pool.borrowObject(key);
            log.info("Borrow request #{}  ...", count);
            Thread.currentThread()
			.sleep(1000);
            pool.returnObject(key, channel1);
            log.info("Return request #{}  ...", count);

        } catch (Exception e) {
            log.error("Exception caught: ", e);
        }
    }

}
