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
package com.ericsson.oss.mediation.camel.jca.ra.datapath.deployment;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.jca.ra.datapath.dependencies.DataPathTestArtifact;
import com.ericsson.oss.mediation.camel.jca.ra.datapath.mock.DataPathTestMockedRoutes;
import com.ericsson.oss.mediation.camel.jca.ra.datapath.test.CamelDataPathTest;

/**
 * Utility class used for deployment creation, any deployment used in test
 * should be created through this class.
 * 
 * @author edejket
 * 
 */
public final class CamelEngineDataPathTestDeployments {

    private static final Logger logger = LoggerFactory
            .getLogger(CamelEngineDataPathTestDeployments.class);

    /**
     * Create RAR deployment from maven artifact
     * 
     * @return rar created by maven
     */
    public static final Archive<?> createCamelEngineDeployment() {
        logger.debug("******Creating camel engine rar for test******");
        final File archiveFile = DataPathTestArtifact
                .resolveArtifactWithoutDependencies(DataPathTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        if (archiveFile == null) {
            throw new IllegalStateException(
                    "Unable to resolve artifact "
                            + DataPathTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        }
        final EnterpriseArchive camelEngineRAR = ShrinkWrap.createFromZipFile(
                EnterpriseArchive.class, archiveFile);

        logger.debug(
                "******Created from maven artifact with coordinates {} ******",
                DataPathTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        return camelEngineRAR;
    }

    /**
     * Create test archive
     * 
     * @return rar created by maven
     */
    public static final Archive<?> createCamelEngineProcessorTestArchive() {
        logger.debug("******Creating test archive******");
        final EnterpriseArchive testEAR = ShrinkWrap.create(
                EnterpriseArchive.class, "mock-mediation-service.ear");
        final JavaArchive testJar = ShrinkWrap.create(JavaArchive.class);
        testJar.addClass(CamelDataPathTest.class);
        testJar.addClass(DataPathTestMockedRoutes.class);
        testJar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        testEAR.addAsLibrary(testJar);
        testEAR.addAsLibrary(DataPathTestArtifact
                .resolveArtifactWithoutDependencies(DataPathTestArtifact.CAMEL_CORE));
        testEAR.addAsLibraries(DataPathTestArtifact
                .resolveArtifactWithoutDependencies(DataPathTestArtifact.MOCKITO_ALL));
        testEAR.addAsManifestResource("jboss-deployment-structure.xml");

        return testEAR;
    }

}
