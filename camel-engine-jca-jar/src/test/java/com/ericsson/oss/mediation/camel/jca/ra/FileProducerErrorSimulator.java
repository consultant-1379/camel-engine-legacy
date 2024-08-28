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
import org.apache.camel.component.file.GenericFileOperationFailedException;

public class FileProducerErrorSimulator implements Processor {
    private final GenericFileOperationFailedException exceptionToSimulate;

    public FileProducerErrorSimulator(final GenericFileOperationFailedException exceptionToSimulate) {
	this.exceptionToSimulate = exceptionToSimulate;
    }

    @Override
    public void process(final Exchange exchange) throws GenericFileOperationFailedException {
	if (exceptionToSimulate != null) {
	    throw exceptionToSimulate;
	}
    }
}