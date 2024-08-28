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

/**
 * ConnectionConfig is used as a key by the connection pooling mechanisms to
 * identify which pooled object belongs to a given target connection.
 * 
 * @author etonayr
 * 
 */
public class ConnectionConfig {

    private final String ipaddress;
    private final Integer port;
    private final String username;
    private final String password;
    private final Boolean keepAlive;
    private final String strictHostKeyChecking;
    private final String secure;

    /**
     * Create configuration for a connection
     * 
     * @param ipaddress
     *            - IpAddress of target machine
     * @param port
     *            - Port of target machine
     * @param username
     *            - user to log on as on target machine
     * @param password
     *            - corresponding user password
     * @param keepAlive
     *            - use connection keep alive, this is only relevant for FTP
     *            connection, it has no effect on SFTP.
     */
    public ConnectionConfig(final String ipaddress, final Integer port,
            final String username, final String password, final String secure) {
        this(ipaddress, port, username, password, secure, "NO", false);
    }

    /**
     * Create configuration for FTPClient creation
     * 
     * @param ipaddress
     *            - IpAddress of target machine
     * @param port
     *            - Port of target machine
     * @param username
     *            - user to log on as on target machine
     * @param password
     *            - corresponding user password
     * @param - try/false as string values, indicates if this connection is FTP
     *        or SFTP
     * @param strictHostChecking
     *            - specifies if StrickHostKeychecking will be used for SFTP
     *            connections - has no effect on FTP connections
     */

    public ConnectionConfig(final String ipaddress, final Integer port,
            final String username, final String password, final String secure,
            final String strictHostChecking) {
        this(ipaddress, port, username, password, secure, strictHostChecking,
                false);
    }

    /**
     * Create configuration for FTPClient creation
     * 
     * @param ipaddress
     *            - IpAddress of target machine
     * @param port
     *            - Port of target machine
     * @param username
     *            - user to log on as on target machine
     * @param password
     *            - corresponding user password
     * @param - try/false as string values, indicates if this connection is FTP
     *        or SFTP
     * @param strictHostChecking
     *            - specifies if StrickHostKeychecking will be used for SFTP
     *            connections - has no effect on FTP connections
     * @param keepAlive
     *            - use connection keep alive, this is only relevant for FTP
     *            connection, it has no effect on SFTP.
     */
    public ConnectionConfig(final String ipaddress, final Integer port,
            final String username, final String password, final String secure,
            final String strictHostChecking, final Boolean keepAlive) {
        this.ipaddress = ipaddress;
        this.username = username;
        this.password = password;
        this.port = port;
        this.strictHostKeyChecking = strictHostChecking;
        this.secure = secure;
        this.keepAlive = keepAlive;
    }

    /**
     * @return the ipaddress
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the keepAlive
     */
    public Boolean getKeepAlive() {
        return keepAlive;
    }

    /**
     * @return the strictHostKeyChecking
     */
    public String getStrictHostKeyChecking() {
        return strictHostKeyChecking;
    }

    /**
     * @return the secure
     */
    public String getSecure() {
        return secure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ConnectionConfig [ipaddress=" + ipaddress + ", port=" + port
                + ", keepAlive=" + keepAlive + ", strictHostKeyChecking="
                + strictHostKeyChecking + ", secure=" + secure + "]";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((ipaddress == null) ? 0 : ipaddress.hashCode());
        result = prime * result
                + ((keepAlive == null) ? 0 : keepAlive.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        result = prime * result + ((secure == null) ? 0 : secure.hashCode());
        result = prime
                * result
                + ((strictHostKeyChecking == null) ? 0 : strictHostKeyChecking
                        .hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConnectionConfig other = (ConnectionConfig) obj;
        if (ipaddress == null) {
            if (other.ipaddress != null) {
                return false;
            }
        } else if (!ipaddress.equals(other.ipaddress)) {
            return false;
        }
        if (keepAlive == null) {
            if (other.keepAlive != null) {
                return false;
            }
        } else if (!keepAlive.equals(other.keepAlive)) {
            return false;
        }
        if (password == null) {
            if (other.password != null) {
                return false;
            }
        } else if (!password.equals(other.password)) {
            return false;
        }
        if (port == null) {
            if (other.port != null) {
                return false;
            }
        } else if (!port.equals(other.port)) {
            return false;
        }
        if (secure == null) {
            if (other.secure != null) {
                return false;
            }
        } else if (!secure.equals(other.secure)) {
            return false;
        }
        if (strictHostKeyChecking == null) {
            if (other.strictHostKeyChecking != null) {
                return false;
            }
        } else if (!strictHostKeyChecking.equals(other.strictHostKeyChecking)) {
            return false;
        }
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

}
