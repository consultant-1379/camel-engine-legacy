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
package com.ericsson.oss.mediation.camel.jca.ra.processor.test;

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

import com.ericsson.oss.mediation.camel.jca.ra.processor.deployment.CamelEngineProcessorTestDeployments;
import com.ericsson.oss.mediation.camel.jca.ra.processor.mock.MockedRoutes;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnection;
import com.ericsson.oss.mediation.datapath.engine.connection.EngineConnectionFactory;

/**
 * Purpose of this test is to verify that mediation service is able to register
 * with mediation router upon deployment, and that router contains relevant
 * information about it in its registry.
 * 
 * @author edejket
 * 
 */
@RunWith(Arquillian.class)
public class CamelProcessorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(CamelProcessorTest.class);

    /**
     * Since we want different scenarios, we will controll arq deployment
     * manually
     * 
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
     * Create Camel engine rar deployment
     * 
     * @return Camel engine rar deployment
     */
    @Deployment(name = "CamelProcessorTest_camel-engine-rar", testable = false, managed = false)
    public static Archive<?> deployCamelEngine() {
        logger.debug("******Getting camel engine rar and deploying it to server******");
        return CamelEngineProcessorTestDeployments
                .createCamelEngineDeployment();
    }

    /**
     * Create test ear deployment
     * 
     * @return EAR containing test classes
     */
    @Deployment(name = "CamelProcessorTest_testEAR", testable = true, managed = false)
    public static Archive<?> deployTestEAR() {
        logger.debug("******Deploying TEST EAR******");
        return CamelEngineProcessorTestDeployments
                .createCamelEngineProcessorTestArchive();
    }

    /**
     * Start executing tests
     */
    @Test
    @InSequence(1)
    @OperateOnDeployment("CamelProcessorTest_camel-engine-rar")
    public void testDeployCamelEngineService() throws Exception {
        this.deployer.deploy("CamelProcessorTest_camel-engine-rar");
        logger.info(" ---------------------------------- DEPLOY camel engine rar ----------------------------------");

    }

    /**
     * Deploy test EAR
     * 
     * @throws Exception
     */
    @Test
    @InSequence(2)
    @OperateOnDeployment("CamelProcessorTest_testEAR")
    public void testDeploy() throws Exception {
        this.deployer.deploy("CamelProcessorTest_testEAR");
        logger.info(" ---------------------------------- DEPLOY test EAR ----------------------------------");

    }

    /**
     * Verify empty datapath throws resource exception
     */
    @Test
    @InSequence(4)
    @OperateOnDeployment("CamelProcessorTest_testEAR")
    public void testEngineInjectionPointIsNotNull() throws Exception {
        logger.info(" ---------------------------------- VERIFY INJECTION POINT IN TEST ----------------------------------");
        Assert.assertNotNull(this.camelEngine);
    }

    /**
     * Verify route can be created with valid processor on class path
     */
    @Test
    @InSequence(5)
    @OperateOnDeployment("CamelProcessorTest_testEAR")
    public void testProcessorLoadingMechanism() throws Exception {
        logger.info(" ---------------------------------- VERIFY PROCESSOR CAN BE LOADED ----------------------------------");
        final EngineConnection connection = this.camelEngine
                .getConnection(MockedRoutes.createDPWithProcessor());
        connection.close();
    }

    /**
     * Verify exception is thrown when processor can not be loaded, as it is not
     * found on class path
     */
    @Test
    @InSequence(6)
    @OperateOnDeployment("CamelProcessorTest_testEAR")
    public void testProcessorLoadingMechanismWhenProcessorNotOnClasspath()
            throws Exception {
        logger.info(" ---------------------------------- VERIFY EXCEPTION WHEN PROCESSOR CAN NOT BE LOADED ----------------------------------");
        final String expectedExceptionMsg = "Processor class com.ericsson.oss.mediation.sets.pm.processors.ProcessorNotOnClasspath was not found on classpath.";
        try {
            final EngineConnection connection = this.camelEngine
                    .getConnection(MockedRoutes
                            .createDPWithProcessorNotOnClassPath());
            fail("Expected exception with  message: " + expectedExceptionMsg);
            connection.close();
        } catch (ResourceException re) {
            logger.error("Exception caught :", re);
            assertEquals(expectedExceptionMsg, re.getCause().getMessage());
        }

    }

    /**
     * Verify exception is thrown when processor can not be loaded, as it has no
     * default constructor
     */
    @Test
    @InSequence(7)
    @OperateOnDeployment("CamelProcessorTest_testEAR")
    public void testProcessorLoadingMechanismWhenProcessorHasNoDefaultConstructor()
            throws Exception {
        logger.info(" ---------------------------------- VERIFY EXCEPTION WHEN PROCESSOR HAS NO DEFAULT CONSTRUCTOR ----------------------------------");
        final String expectedExceptionMsg = "Unable to create processor instance of type com.ericsson.oss.mediation.camel.jca.ra.processor.mock.DummyProcessorWithNoPublicConstructor processor must have default constructor.";
        try {
            final EngineConnection connection = this.camelEngine
                    .getConnection(MockedRoutes
                            .createDPWithProcessorWithoutDefaultConstructor());
            fail("Expected exception with  message: " + expectedExceptionMsg);
            connection.close();
        } catch (ResourceException re) {
            logger.error("Exception caught :", re);
            assertEquals(expectedExceptionMsg, re.getCause().getMessage());
        }

    }

    /**
     * Undeploy camel engine rar
     */
    @Test
    @InSequence(8)
    @OperateOnDeployment("CamelProcessorTest_camel-engine-rar")
    public void testUndeployCamelEngine() {
        this.deployer.undeploy("CamelProcessorTest_camel-engine-rar");
        logger.info(" ---------------------------------- UNDEPLOY camel engine rar ----------------------------------");

    }

}
