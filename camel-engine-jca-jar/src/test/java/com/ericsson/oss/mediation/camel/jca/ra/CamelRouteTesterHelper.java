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

import java.util.*;

// TODO:Currently, operating on the native JAXB components. When TT393 is 
//      checked into master this code shall need to be refactored to operate
//      against the data path elements that wrap the native JAXB objects
public class CamelRouteTesterHelper {
    private static Map<String, String> properties = new HashMap<String, String>();
    private static Map<String, Object> headers = new HashMap<String, Object>();

    // Just a sample set of valid properties. Doesn't matter much what these
    // are as the Data Path is going to be mocked and not do anything real.
    static {
        properties.put("ipAddress", "10.45.239.32");
        properties.put("port", "21");
        properties.put("user", "LTE01ERBS00001");
        properties.put("password", "secret");
        properties.put("queueName", "NonModeledMediationResponseQueue_1");
        properties.put("directory", "/");
    }

    // Doesn't matter what these values are as shall not be doing anything real
    static {
        headers.put("sourceFileName", "sourceFileNameValue");
        headers.put("sourceDirectory", "sourceFileNameValue");
        headers.put("destinationFileName", "sourceFileNameValue");
        headers.put("destinationDirectory", "sourceFileNameValue");
        headers.put("jobId", "sourceFileNameValue");
        headers.put("jobStartTime", new Date().getTime());
    }

    public static Map<String, Object> createExchangeHeaders() {
        return Collections.unmodifiableMap(headers);
    }

}
