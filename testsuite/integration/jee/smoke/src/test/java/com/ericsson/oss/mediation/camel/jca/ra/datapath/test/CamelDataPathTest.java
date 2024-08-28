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
package com.ericsson.oss.mediation.camel.jca.ra.datapath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.annotation.Resource;
import javax.resource.ResourceException;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.jca.ra.datapath.deployment.CamelEngineDataPathTestDeployments;
import com.ericsson.oss.mediation.camel.jca.ra.datapath.mock.DataPathTestMockedRoutes;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnection;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnectionFactory;

/**
 * Purpose of this tests is to verify basic datapath functionality, using basic
 * smoke tests.
 * 
 * @author edejket
 * 
 */
@RunWith(Arquillian.class)
public class CamelDataPathTest {

    private static final Logger logger = LoggerFactory
            .getLogger(CamelDataPathTest.class);

    /**
     * Arquillian related injections
     */
    @ArquillianResource
    private ContainerController controller;

    @ArquillianResource
    private Deployer deployer;

    /**
     * Test related injections
     */

    @Resource(lookup = "java:/eis/CamelConnectionFactory_1")
    private EngineConnectionFactory camelEngine;

    /**
     * Create Camel engine rar deployment for test, will take maven artifact
     * from maven repository.
     * 
     * @return Camel engine rar deployment
     */
    @Deployment(name = "CamelDataPathTest_camel-engine-rar", testable = false, managed = false)
    public static Archive<?> deployCamelEngine() {
        logger.debug("******Getting camel engine rar and deploying it to server******");
        return CamelEngineDataPathTestDeployments.createCamelEngineDeployment();
    }

    /**
     * Create test ear deployment, will simulate mediation-service.ear
     * 
     * @return EAR containing test classes
     */
    @Deployment(name = "CamelDataPathTest_testEAR", testable = true, managed = false)
    public static Archive<?> deployTestEAR() {
        logger.debug("******Deploying TEST EAR(simultation of mediation-service)******");
        return CamelEngineDataPathTestDeployments
                .createCamelEngineProcessorTestArchive();
    }

    /**
     * Start executing tests
     */

    /**
     * Deploy camel engine
     * 
     * @throws Exception
     */
    @Test
    @InSequence(1)
    @OperateOnDeployment("CamelDataPathTest_camel-engine-rar")
    public void testDeployCamelEngineService() throws Exception {
        this.deployer.deploy("CamelDataPathTest_camel-engine-rar");
        logger.info(" ---------------------------------- DEPLOY camel engine rar ----------------------------------");

    }

    /**
     * Deploy test EAR, that will simulate mediation-service.ear
     * 
     * @throws Exception
     */
    @Test
    @InSequence(2)
    @OperateOnDeployment("CamelDataPathTest_testEAR")
    public void testDeploy() throws Exception {
        this.deployer.deploy("CamelDataPathTest_testEAR");
        logger.info(" ---------------------------------- DEPLOY test EAR ----------------------------------");

    }

    /**
     * Verify empty datapath throws resource exception
     */
    @Test
    @InSequence(3)
    @OperateOnDeployment("CamelDataPathTest_testEAR")
    public void testEngineInjectionPointIsNotNull() throws Exception {
        logger.info(" ---------------------------------- VERIFY INJECTION POINT IN TEST ----------------------------------");
        Assert.assertNotNull(this.camelEngine);
    }

    /**
     * Verify exception is thrown when route can not be created, as it has no
     * elements (route with no elements smoke test)
     */
    @Test
    @InSequence(4)
    @OperateOnDeployment("CamelDataPathTest_testEAR")
    public void testExceptionThrownWhenDatpathIsEmpty() throws Exception {
        logger.info(" ---------------------------------- VERIFY EXCEPTION ON EMPTY DATAPATH ----------------------------------");
        final String expectedExceptionMsg = "Unable to create camel route, datapath element list is empty.";
        try {
            final EngineConnection connection = this.camelEngine
                    .getConnection(DataPathTestMockedRoutes
                            .createDPWithNoElements());
            fail("Expected excpetion with  message: " + expectedExceptionMsg);
            connection.close();
        } catch (ResourceException re) {
            assertEquals(expectedExceptionMsg, re.getCause().getMessage());
        }
    }

    /**
     * Undeploy engine
     */
    @Test
    @InSequence(5)
    @OperateOnDeployment("CamelDataPathTest_camel-engine-rar")
    public void testUndeployCamelEngine() {
        this.deployer.undeploy("CamelDataPathTest_camel-engine-rar");
        logger.info(" ---------------------------------- UNDEPLOY camel engine rar ----------------------------------");

    }

}
