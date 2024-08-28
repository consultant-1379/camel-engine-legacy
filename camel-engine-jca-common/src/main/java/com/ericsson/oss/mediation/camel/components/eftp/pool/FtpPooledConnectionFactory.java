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
package com.ericsson.oss.mediation.camel.components.eftp.pool;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.components.eftp.pool.exception.GenericPoolException;

/**
 * 
 * Object factory for use with Commons Pooling. This factory manages the
 * lifecyle of FTPClient connections, it is used by the FtpConnectionPool class.
 * 
 * 
 * @author etonayr
 * 
 */
public class FtpPooledConnectionFactory extends
        BaseKeyedPoolableObjectFactory<ConnectionConfig, FTPClient> {

    private static final Logger LOG = LoggerFactory
            .getLogger(FtpPooledConnectionFactory.class);
    
    private static FtpPooledConnectionFactory poolFactory = null;
    
    
    private FtpPooledConnectionFactory()
    {
    	super();
    }
    
    public static final FtpPooledConnectionFactory getInstance() {
		if (poolFactory == null) {
			poolFactory = new FtpPooledConnectionFactory();
		}
		return poolFactory;
	}

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.commons.pool.BaseKeyedPoolableObjectFactory#makeObject(java
     * .lang.Object)
     */
    @Override
    public FTPClient makeObject(final ConnectionConfig config)
            throws GenericPoolException {
        LOG.debug("creating new object in ftp pool with key [{}]", config);
        final FTPClient ftpClient = new FTPClient();
        final FTPClientConfig conf = new FTPClientConfig(
                FTPClientConfig.SYST_UNIX);
        try {
            ftpClient.configure(conf);
            ftpClient.connect(config.getIpaddress(), config.getPort());
            final boolean loggedIn = ftpClient.login(config.getUsername(),
                    config.getPassword());

            if (!loggedIn) {
                throw new GenericPoolException(ftpClient.getReplyCode(),
                        "Could not log in to ftp session with credentials supplied");
            }
            ftpClient.setRemoteVerificationEnabled(false);
            ftpClient.setSoTimeout(Constants.FTP_SOCKET_TIMEOUT);
            ftpClient.setKeepAlive(config.getKeepAlive());

        } catch (final SocketException e) {
            LOG.error(
                    "SocketException detected during connection, has error message: [{}]",
                    e.getMessage());
            throw new GenericPoolException(ftpClient.getReplyCode(),
                    ftpClient.getReplyString()
                            + " , Connection Config Details [ " + config + "]",
                    e);
        } catch (final IOException e) {
            LOG.error(
                    "IOException detected during connection, has error message: [{}]",
                    e.getMessage());
            throw new GenericPoolException(ftpClient.getReplyCode(),
                    ftpClient.getReplyString(), e);
        }

        return ftpClient;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.commons.pool.BaseKeyedPoolableObjectFactory#validateObject
     * (java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean validateObject(final ConnectionConfig config,
            final FTPClient client) {
        LOG.debug("validateObject");
        try {
            final boolean validated = (client.isConnected() && client
                    .sendNoOp());
            LOG.debug("ValidateObject returning {}", validated);
            return validated;
        } catch (final Exception e) {
            LOG.debug("Exception caught during validate, returning false");
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.commons.pool.BaseKeyedPoolableObjectFactory#activateObject
     * (java.lang.Object, java.lang.Object)
     */
    @Override
    public void activateObject(final ConnectionConfig config,
            final FTPClient client) throws GenericPoolException {
        LOG.debug("activateObject for key [{}]", config);
        try {
            if (!client.sendNoOp()) {
                throw new GenericPoolException(-1,
                        "Error while invoking activateObject");
            }
        } catch (final IOException e) {
            LOG.error(
                    "IOException detected during activate object, has error message: [{}]",
                    e.getMessage());
            throw new GenericPoolException(
                    "Error while invoking activateObject", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.commons.pool.BaseKeyedPoolableObjectFactory#destroyObject(
     * java.lang.Object, java.lang.Object)
     */
    @Override
    public void destroyObject(final ConnectionConfig config,
            final FTPClient client) throws GenericPoolException {
        LOG.debug("destroyObject for key [{}] ", config);
        try {
            client.logout();
            client.disconnect();
        } catch (final IOException e) {
            LOG.error(
                    "IOException detected during destroyObject, has error message: [{}]",
                    e.getMessage());
            throw new GenericPoolException(client.getReplyCode(),
                    "Error closing ftp session with config [" + config + "]", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.commons.pool.BaseKeyedPoolableObjectFactory#passivateObject
     * (java.lang.Object, java.lang.Object)
     */
    @Override
    public void passivateObject(final ConnectionConfig config,
            final FTPClient client) throws GenericPoolException {
        LOG.debug("pasivateObject for key [{}] ", config);
    }

}
