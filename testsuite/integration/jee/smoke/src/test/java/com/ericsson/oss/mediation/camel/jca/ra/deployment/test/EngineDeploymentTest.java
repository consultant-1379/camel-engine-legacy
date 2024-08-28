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
package com.ericsson.oss.mediation.camel.jca.ra.deployment.test;

import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.jca.ra.deployment.test.deployment.CamelEngineTestDeployments;

/**
 * Purpose of this test is to verify that mediation service is able to register
 * with mediation router upon deployment, and that router contains relevant
 * information about it in its registry.
 * 
 * @author edejket
 * 
 */
@RunWith(Arquillian.class)
public class EngineDeploymentTest {

    private static final Logger logger = LoggerFactory
            .getLogger(EngineDeploymentTest.class);

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
     * Create ModelService deployment
     * 
     * @return ModelService.ear deployment
     */
    @Deployment(name = "camel-engine-rar", testable = false, managed = false)
    public static Archive<?> depoloyModelService() {
        logger.debug("******Getting camel engine rar and deploying it to server******");
        return CamelEngineTestDeployments.createCamelEngineDeployment();
    }

    /**
     * Start executing tests
     */
    @Test
    @InSequence(1)
    @OperateOnDeployment("camel-engine-rar")
    public void testDeployModelService() throws Exception {
        this.deployer.deploy("camel-engine-rar");
        logger.info(" ---------------------------------- DEPLOY camel engine rar ----------------------------------");

    }

    @Test
    @InSequence(2)
    @OperateOnDeployment("camel-engine-rar")
    public void deployMediationCore() {
        this.deployer.undeploy("camel-engine-rar");
        logger.info(" ---------------------------------- UNDEPLOY camel engine rar ----------------------------------");

    }

}
