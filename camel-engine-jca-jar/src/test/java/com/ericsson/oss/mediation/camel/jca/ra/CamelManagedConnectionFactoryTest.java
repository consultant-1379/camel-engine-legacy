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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ericsson.oss.mediation.datapath.definition.DataPathDefinition;

@RunWith(MockitoJUnitRunner.class)
public class CamelManagedConnectionFactoryTest {

    @Mock
    private DataPathDefinition pathDefinitionOne;

    @Mock
    private DataPathDefinition pathDefinitionTwo;

    @Mock
    private ConnectionManager cxManager;

    private final CamelManagedConnectionFactory factory = new CamelManagedConnectionFactory();

    @Test
    public void matchingConnection_EmptySetProvided_ReturnsNull()
            throws Exception {
        final CamelConnectionRequestInfo requestOne = getCamelConnectionRequestInfo(pathDefinitionOne);
        final ManagedConnection connection = factory.matchManagedConnections(
                Collections.emptySet(), null, requestOne);
        assertNull(connection);
    }

    @Test
    public void matchingConnection_ConnectionInSet_ReturnsMatchedConnection()
            throws Exception {
        final CamelConnectionRequestInfo requestOne = getCamelConnectionRequestInfo(pathDefinitionOne);
        final Set<ManagedConnection> managedConections = new HashSet<>();
        final CamelManagedConnection managedConnection = new CamelManagedConnection(
                factory, requestOne);
        managedConections.add(managedConnection);

        // note: has the same path definition!
        final CamelConnectionRequestInfo requestTwo = getCamelConnectionRequestInfo(pathDefinitionOne);

        final ManagedConnection connection = factory.matchManagedConnections(
                managedConections, null, requestTwo);
        assertNotNull(connection);
    }

    private CamelConnectionRequestInfo getCamelConnectionRequestInfo(
            final DataPathDefinition pathDefinition) throws Exception {
        List<DataPathDefinition> list = new LinkedList<>();
        list.add(pathDefinition);
        return new CamelConnectionRequestInfo(list);
    }

}
