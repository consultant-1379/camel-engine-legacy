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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.*;
import org.apache.sshd.server.auth.UserAuthPassword;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.filesystem.NativeFileSystemFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.components.eftp.pool.ConnectionConfig;
import com.ericsson.oss.mediation.camel.components.eftp.pool.SftpConnectionPool;

@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public class PoolConfigurationTest extends CamelTestSupport {

    private static final Logger log = LoggerFactory
            .getLogger(PoolConfigurationTest.class);

    private static SftpConnectionPool sftpPool;

    private static SshServer sshd;
    private static final String USERNAME = "test";
    private static final String PASSWD = "secret";
    private static final int PORT = 58558;

    @BeforeClass
    public static void setUpSftpServer() {

        final int poolSize = 2;
        final int maxActive = 1;
        final int maxIdle = 1;
        final long waitTime = 800000;
        final long evictionEligibleAfter = 10000;
        final long evictionThreadRunTime = 5000;
        final int numTestsPerEvictionRun = 50;

        sftpPool = new SftpConnectionPool(maxActive, maxIdle, poolSize,
                waitTime, evictionEligibleAfter, evictionThreadRunTime,
                numTestsPerEvictionRun);

        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(PORT);
        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            @Override
            public boolean authenticate(final String username,
                    final String password, final ServerSession session) {
                if (username.equals(USERNAME) && password.equals(PASSWD)) {
                    return true;
                }
                return false;
            }
        });
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setFileSystemFactory(new NativeFileSystemFactory());
        sshd.setCommandFactory(new ScpCommandFactory());

        final List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>(
                1);
        userAuthFactories.add(new UserAuthPassword.Factory());
        sshd.setUserAuthFactories(userAuthFactories);

        final List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();

        namedFactoryList.add(new SftpSubsystem.Factory());
        sshd.setSubsystemFactories(namedFactoryList);

        try {
            sshd.start();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.test.junit4.CamelTestSupport#createRegistry()
     */
    @Override
    protected JndiRegistry createRegistry() throws Exception {
        final JndiRegistry registry = super.createRegistry();
        registry.bind("sftpPool", sftpPool);
        return registry;
    }

    public void setUpSftpPool() throws Exception {
        log.info("Setting up the test...");

    }

    void shutdownAndAwaitTermination(final ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testPoolBorrow() throws Exception {
        final SftpConnectionPool pool = (SftpConnectionPool) this.context
                .getRegistry().lookup("sftpPool");

        final ConnectionConfig key1 = new ConnectionConfig("localhost", PORT,
                USERNAME, PASSWD, "true");

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final ExecutorService executor1 = Executors.newFixedThreadPool(10);
        for (int i = 1; i < 5; i++) {
            final ClientThread ct1 = new ClientThread(pool, key1, i + "");
            final ClientThread ct2 = new ClientThread(pool, key1, i + ":a");
            executor.execute(ct1);
            executor1.execute(ct2);
        }
        shutdownAndAwaitTermination(executor);
    }
}