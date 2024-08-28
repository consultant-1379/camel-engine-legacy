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
package com.ericsson.oss.mediation.camel.jca.ra.processor.mock;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DummyProcessorWithNoPublicConstructor implements Processor {

    private final String arg1;
    private final String arg2;

    public DummyProcessorWithNoPublicConstructor(final String arg1,
            final String arg2) {
        super();
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO Auto-generated method stub

    }

}
