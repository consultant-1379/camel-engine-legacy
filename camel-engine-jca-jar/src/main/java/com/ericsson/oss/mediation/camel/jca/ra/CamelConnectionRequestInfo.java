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

import java.util.List;

import javax.resource.spi.ConnectionRequestInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;

/**
 * Generated class, ConnectionRequestInfo holds information about connection
 * request, in this case, it will hold information about DataPathDefinition
 * 
 * @author edejket
 * 
 */
public class CamelConnectionRequestInfo implements ConnectionRequestInfo {

    private static final Logger LOG = LoggerFactory
            .getLogger(CamelConnectionRequestInfo.class);

    private final List<DataPathDefinition> pathDefinition;

    public CamelConnectionRequestInfo(final List<DataPathDefinition> pathDef) {
        LOG.debug("Constructing CamelConnectionRequestInfo...");
        this.pathDefinition = pathDef;
    }

    List<DataPathDefinition> getPathDef() {
        return pathDefinition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((pathDefinition == null) ? 0 : pathDefinition.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CamelConnectionRequestInfo)) {
            return false;
        }
        final CamelConnectionRequestInfo other = (CamelConnectionRequestInfo) obj;
        if (pathDefinition == null) {
            if (other.pathDefinition != null) {
                return false;
            }
        } else if (!pathDefinition.equals(other.pathDefinition)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String out = pathDefinition == null ? "pathDefinition=null"
                : "pathDefinition=" + pathDefinition.get(0).getId();
        return "CamelConnectionRequestInfo=[" + out + "]";
    }

}
