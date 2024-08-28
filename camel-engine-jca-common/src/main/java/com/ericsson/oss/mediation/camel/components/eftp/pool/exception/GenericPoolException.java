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
package com.ericsson.oss.mediation.camel.components.eftp.pool.exception;

/**
 * Generic exception thrown by pool operations
 * 
 * @author edejket
 * 
 */
public class GenericPoolException extends Exception {

    /**
     * 
     */

    private static final long serialVersionUID = 463265296624981567L;

    private int code;
    private String reason;

    public GenericPoolException(final String str) {
        super(str);
    }

    public GenericPoolException(final Exception e) {
        super(e);
    }

    public GenericPoolException(final int code, final String reason) {
        super();
        this.code = code;
        this.reason = reason;
    }

    public GenericPoolException(final int code, final String reason,
            final Throwable t) {
        super(reason, t);
        this.code = code;
        this.reason = reason;
    }

    public GenericPoolException(final String reason, final Throwable t) {
        super(reason, t);
        this.reason = reason;
    }

    /**
     * @return the code
     */
    public final int getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public final void setCode(final int code) {
        this.code = code;
    }

    /**
     * @return the reason
     */
    public final String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public final void setReason(final String reason) {
        this.reason = reason;
    }

}
