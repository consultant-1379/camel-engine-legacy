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

/**
 * Used in the <code>CamelRouteBuilderTest</code> as a utility for stubbing out
 * the JMS producer in the data path.
 * 
 * @author eleejhn
 */
public class JMSProducerStub implements Processor {
    private Object body;

    @Override
    public void process(final Exchange exchange) throws Exception {
        this.body = exchange.getIn().getBody();
        exchange.getOut().setBody(body);
    }

    public Object getBody() {
        return body;
    }
}
