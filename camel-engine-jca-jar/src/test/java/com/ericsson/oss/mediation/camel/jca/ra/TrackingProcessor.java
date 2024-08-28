package com.ericsson.oss.mediation.camel.jca.ra;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.junit.Assert;

public class TrackingProcessor implements Processor {

    private Object receivedBody = null;
    private Object expectedBody = null;
    private Exception exceptionToThrow = null;

    public void setExpectedBody(final Object expectedBody) {
        assert expectedBody != null;
        this.expectedBody = expectedBody;
        this.receivedBody = null;
    }

    public void throwException(final Exception exceptionToThrow) {
        this.exceptionToThrow = exceptionToThrow;
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        if (exceptionToThrow != null) {
            final Exception cachedException = exceptionToThrow;
            exceptionToThrow = null;
            throw cachedException;
        }
        this.receivedBody = exchange.getIn().getBody();
    }

    public void verify() {
        Assert.assertEquals(expectedBody, receivedBody);
    }
}
