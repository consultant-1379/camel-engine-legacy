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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.ericsson.oss.mediation.camel.components.eftp.exceptions.GenericEftpException;

/**
 * Used in the <code>CamelRouteBuilderTest</code> as a utility for stubbing out the Eftp Component in the data path and
 * generating error scenarios.
 * 
 * @author eleejhn
 */
public class EftpProducerErrorSimulator implements Processor {
    private final GenericEftpException exceptionToSimulate;

    public EftpProducerErrorSimulator(final GenericEftpException exceptionToSimulate) {
	// We allow null to be passed, in cases where we just want to stub
	// out the eFTP component and not transfer anything
	this.exceptionToSimulate = exceptionToSimulate;
    }

    @Override
    public void process(final Exchange exchange) throws GenericEftpException {
	if (exceptionToSimulate != null) {
	    throw exceptionToSimulate;
	}
    }
}

