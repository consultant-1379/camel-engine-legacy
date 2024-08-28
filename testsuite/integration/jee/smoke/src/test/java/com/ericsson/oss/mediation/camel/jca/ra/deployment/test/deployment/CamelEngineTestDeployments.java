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
package com.ericsson.oss.mediation.camel.jca.ra.deployment.test.deployment;

import java.io.File;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.mediation.camel.jca.ra.deployment.test.dependencies.DeploymentTestArtifact;

/**
 * Utility class used for deployment creation, any deployment used in test
 * should be created through this class.
 * 
 * @author edejket
 * 
 */
public final class CamelEngineTestDeployments {

    private static final Logger logger = LoggerFactory
            .getLogger(CamelEngineTestDeployments.class);

    /**
     * Create RAR deployment from maven artifact
     * 
     * @return rar created by maven
     */
    public static final Archive<?> createCamelEngineDeployment() {
        logger.debug("******Creating camel engine rar for test******");
        final File archiveFile = DeploymentTestArtifact
                .resolveArtifactWithoutDependencies(DeploymentTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        if (archiveFile == null) {
            throw new IllegalStateException("Unable to resolve artifact "
                    + DeploymentTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        }
        final EnterpriseArchive camelEngineRAR = ShrinkWrap.createFromZipFile(
                EnterpriseArchive.class, archiveFile);

        logger.debug(
                "******Created from maven artifact with coordinates {} ******",
                DeploymentTestArtifact.COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR);
        return camelEngineRAR;
    }

}
