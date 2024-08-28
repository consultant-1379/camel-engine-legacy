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

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.components.eftp.pool.exception.GenericPoolException;
import com.jcraft.jsch.*;

/**
 * 
 * Object factory for use with Commons Pooling. This factory manages the
 * lifecyle of SSH connections connections, it is used by the SftpConnectionPool
 * class.
 * 
 * 
 * @author etonayr
 * 
 */
public class SftpPooledConnectionFactory extends
        BaseKeyedPoolableObjectFactory<ConnectionConfig, ChannelSftp> {

    private static final Logger log = LoggerFactory
            .getLogger(SftpPooledConnectionFactory.class);

    private static SftpPooledConnectionFactory poolFactory = null;

    public static final SftpPooledConnectionFactory getInstance() {
        if (poolFactory == null) {
            poolFactory = new SftpPooledConnectionFactory();
        }
        return poolFactory;
    }

    private SftpPooledConnectionFactory() {
        super();
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
            final ChannelSftp channel) throws GenericPoolException {
        if (!channel.isConnected()) {
            log.debug(
                    "activateObject: Channel {} is not connected, trying to connect...",
                    config);
            try {
                openSftpChannel(channel.getSession());
            } catch (final JSchException e) {
                log.error(
                        "Exception caught while trying to reconnect the channel in activateObject method call, stack trace is:",
                        e);
                throw new GenericPoolException("Error activating object ["
                        + config + "] in connection pool", e);
            }
        }

    }

    /**
     * Connect SSH session instance, before connecting it will set default
     * attributes on the session.
     * 
     * @see SftpPooledConnectionFactory#createSSHSession(ConnectionConfig)
     * @param session
     *            Session
     * @throws JSchException
     */
    private void connectSSHSession(final Session session) throws JSchException {
        try {
            setSessionDefaultAttributes(session);
            session.connect(Constants.SESSION_CONNECT_TIMEOUT);
        } catch (JSchException jsche) {
            log.error("connectSSHSession ssh error:", jsche);
            throw jsche;
        } catch (Exception e) {
            log.error("connectSSHSession ssh error:", e);
            throw new JSchException(e.getMessage(), e);
        }
    }

    /**
     * Create new SSH session and set ConnectionConfig attributes on this
     * session. This method will set <code>STRICT_HOST_KEY_CHECKING</code> and
     * <code>password<code> values from ConnectionConfig key.
     * Created ssh session is not yet connected at this point.
     * 
     * @param key
     *            ConnectinoConfig key that will be used to create new instance
     *            of the SSH session
     * @return Session instance
     * @throws JSchException
     */
    private Session createSSHSession(final ConnectionConfig key)
            throws JSchException {
        try {
            final JSch jsch = new JSch();
            final Session session = jsch.getSession(key.getUsername(),
                    key.getIpaddress(), key.getPort());
            session.setConfig(Constants.STRICT_HOST_KEY_CHECKING,
                    key.getStrictHostKeyChecking());
            session.setPassword(key.getPassword());
            return session;
        } catch (JSchException jsche) {
            throw jsche;
        } catch (Exception e) {
            throw new JSchException("createSSHSession generic error", e);
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
            final ChannelSftp channel) throws GenericPoolException {
        log.debug("destroyObject method called for key=[{}]", config);
        try {
            final Session session = channel.getSession();
            if (channel.isConnected()) {
                log.debug(
                        "destroyObject [{}], channel is connected calling disconnect",
                        config);
                channel.disconnect();
            }
            if (session.isConnected()) {
                log.debug("destroyObject [{}], session is connected, calling disconnect");
                session.disconnect();
            }
        } catch (JSchException jsche) {
            log.error("Error trying to destroy channel for key=[{}]", config,
                    jsche);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.commons.pool.BaseKeyedPoolableObjectFactory#makeObject(java
     * .lang.Object)
     */
    @Override
    public ChannelSftp makeObject(final ConnectionConfig key)
            throws JSchException {
        log.debug("creating new object in ssh pool with key [{}]", key);
        final Session session = createSSHSession(key);
        connectSSHSession(session);
        final ChannelSftp channel = openSftpChannel(session);
        return channel;
    }

    /**
     * This method will open sftp channel for given session.
     * 
     * @param session
     *            Jsch session
     * @return Jsch channel ready to be used
     * @throws JSchException
     */
    public ChannelSftp openSftpChannel(final Session session)
            throws JSchException {
        final ChannelSftp channel = (ChannelSftp) session
                .openChannel(Constants.SFTP_CHANNEL_TYPE);
        log.debug("Verifying that session is still connected before trying to open a channel...");

        if (!session.isConnected()) {
            log.debug("Session is not connected, trying to reconnect...");
            try {
                session.connect(Constants.SESSION_CONNECT_TIMEOUT);
            } catch (JSchException jsche) {
                log.error(
                        "Unable to reconnect the session, will not be able to open channel:",
                        jsche);
                throw jsche;
            }
        }

        try {
            if (!channel.isConnected()) {
                channel.connect(Constants.CHANNEL_CONNECT_TIMEOUT);
            }
            return channel;
        } catch (JSchException jsche) {
            log.error("openSftpChannel, error opening channel:", jsche);
            if (session.isConnected()) {
                session.disconnect();
            }
            throw jsche;

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
            final ChannelSftp channel) throws GenericPoolException {
        log.debug("pasivateObject method called for channel with key=[{}]",
                config);
    }

    /**
     * Set predefined seto of attributes for this session Enable socket timeout
     * of SOCKET_TIMEOUT in milis, default value is 10 seconds
     * 
     * @param session
     *            JSCH Session
     * @throws JSchException
     */
    private void setSessionDefaultAttributes(final Session session)
            throws JSchException {
        session.setTimeout(Constants.SFTP_SOCKET_TIMEOUT);
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
            final ChannelSftp channel) {
        try {
            if (channel == null || !channel.getSession().isConnected()) {
                if (log.isDebugEnabled()) {
                    log.debug("Channel for key=[{}], is NOT valid", config);
                }
                return false;
            }
            if (log.isDebugEnabled()) {
                log.debug("Channel for key=[{}], is valid", config);
            }
            return true;
        } catch (JSchException jsche) {
            if (log.isDebugEnabled()) {
                log.debug("Error validating channel for key=[{}].", config);
            }
            return false;
        }

    }
}
