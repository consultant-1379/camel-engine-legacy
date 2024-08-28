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
 * Base class for all custom Camel processors, this exception should be used
 * instead throwing Exception
 * 
 * @author edejket
 * 
 */
public class EProcessorException extends Exception {

    private static final long serialVersionUID = -2283020277508947959L;

    /**
     * Constructor with String argument describing exception
     * 
     * @param msg
     *            String cause/description of exception
     */
    public EProcessorException(final String msg) {
        super(msg);
    }

    /**
     * Constructor with Throwable argument
     * 
     * @param thr
     *            Throwable cause
     * @param msg
     *            String cause/description of exception
     */
    public EProcessorException(final Throwable thr, final String msg) {
        super(msg, thr);
    }

}
