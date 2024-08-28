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
package com.ericsson.oss.mediation.camel.jca.ra.processor.deployment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.jca.ra.processor.dependencies.ProcessorTestArtifact;
import com.ericsson.oss.mediation.camel.jca.ra.processor.mock.DummyProcessorWithNoPublicConstructor;
import com.ericsson.oss.mediation.camel.jca.ra.processor.mock.MockedRoutes;
import com.ericsson.oss.mediation.camel.jca.ra.processor.test.CamelProcessorTest;

/**
 * Utility class used for deployment creation, any deployment used in test
 * should be created through this class.
 * 
 * @author edejket
 * 
 */
public final class CamelEngineProcessorTestDeployments {

    private static final Logger logger = LoggerFactory
            .getLogger(CamelEngineProcessorTestDeployments.class);

    /**
     * Create RAR deployment from maven artifact
     * 
     * @return rar created by maven
     */
    public static final Archive<?> createCamelEngineDeployment() {
        logger.debug("******Creating camel engine rar for test******");
        final File archiveFile = ProcessorTestArtifact
                .resolveArtifactWithoutDependencies(ProcessorTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        if (archiveFile == null) {
            throw new IllegalStateException(
                    "Unable to resolve artifact "
                            + ProcessorTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        }
        final ResourceAdapterArchive camelEngineRAR = ShrinkWrap
                .createFromZipFile(ResourceAdapterArchive.class, archiveFile);
        /**
         * add invalid processors jar
         * 
         */
        camelEngineRAR.addAsLibrary(createInvalidProcessorsJar());
        logger.debug(
                "******Created from maven artifact with coordinates {} ******",
                ProcessorTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        return camelEngineRAR;
    }

    public static final Archive<?> createInvalidProcessorsJar() {
        Collection<Class<?>> invalidProcessors = new ArrayList<>();
        invalidProcessors.add(DummyProcessorWithNoPublicConstructor.class);
        return createTestProcessorsArchive("invalid-processors.jar",
                invalidProcessors);
    }

    public static final Archive<?> createTestProcessorsArchive(
            final String archiveName, Collection<Class<?>> processors) {
        logger.debug("******Creating " + archiveName + " jar******");
        final JavaArchive jarArchive = ShrinkWrap.create(JavaArchive.class,
                archiveName);
        for (Class<?> clazz : processors) {
            jarArchive.addClass(clazz);
        }
        return jarArchive;
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
        testJar.addClass(CamelProcessorTest.class);
        testJar.addClass(MockedRoutes.class);
        testJar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        testEAR.addAsLibrary(testJar);
        testEAR.addAsLibrary(createInvalidProcessorsJar());
        testEAR.addAsLibrary(ProcessorTestArtifact
                .resolveArtifactWithoutDependencies(ProcessorTestArtifact.CAMEL_CORE));
        testEAR.addAsLibraries(ProcessorTestArtifact
                .resolveArtifactWithoutDependencies(ProcessorTestArtifact.MOCKITO_ALL));
        testEAR.addAsManifestResource("jboss-deployment-structure.xml");

        return testEAR;
    }
}
