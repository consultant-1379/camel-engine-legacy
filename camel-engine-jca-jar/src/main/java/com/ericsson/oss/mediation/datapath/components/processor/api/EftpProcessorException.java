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
package com.ericsson.oss.mediation.datapath.components.processor.api;

/**
 * <p>
 * A processor exception which captures FTP errors that occur outside of the
 * <code>EventDrivenFtpProducer</code>.
 * </p>
 * 
 * @author eleejhn
 */
public class EftpProcessorException extends EProcessorException {
    private static final long serialVersionUID = 8536704872694379092L;
    private final int errorCode;

    /**
     * @param message
     *            the description of the error that has occurred
     * @param errorCode
     *            associated with the error that has occurred
     */
    public EftpProcessorException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * 
     * @param throwable
     *            wrapped <code>Exception</code>
     * @param message
     *            the description of the error that has occurred
     * @param errorCode
     *            associated with the error that has occurred
     */
    public EftpProcessorException(final Throwable throwable,
            final String message, final int errorCode) {
        super(throwable, message);
        this.errorCode = errorCode;
    }

    /**
     * @return errorCode associated with this <code>Exception</code>
     */
    public int getErrorCode() {
        return errorCode;
    }
}
