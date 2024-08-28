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
 * Constants used in common pool module
 * Store any constants related to sftp/ftp pools
 * 
 * @author edejket
 * 
 */
public class Constants {

    /**
     * SFTP Pool name
     */
    public static final String SFTP_POOL = "sftpPool";
    
    /**
     * Strict host key check option
     */
    public static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    
    /**
     * Name of the channel for sftp connections
     */
    public static final String SFTP_CHANNEL_TYPE = "sftp";
	
	/**
	 * SFTP socket timeout default value
	 */
    public static final int SFTP_SOCKET_TIMEOUT = 120000;
	
	/**
	 * SFTP session timeout default value
	 */
    public static final int SESSION_CONNECT_TIMEOUT = 120000;
	
	/**
	 * SFTP channel connect default timeout
	 */
    public static final int CHANNEL_CONNECT_TIMEOUT = 120000;

    /**
     * FTP Pool name
     */
    public static final String FTP_POOL = "ftpPool";
    
    /**
     * FTP default socket timeout value
     */
    public static final int FTP_SOCKET_TIMEOUT = 120000;
    
    
    
}
