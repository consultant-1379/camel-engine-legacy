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

package com.ericsson.oss.mediation.camel.jca.ra.deployment.test.dependencies;

import java.io.File;

import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * <b>Dependencies and artifacts used in FTPFileCollectionTest</b> </br>This
 * class contains all dependencies used in this test listed as constants for
 * easier use. Dependencies should be added in sections, for service framework,
 * this project artifacts etc...</br> When adding dependencies here, make sure
 * same are added into pom file as well. </br><b>It is forbidden to put versions
 * of dependencies here all versions need to be defined in pom files, this list
 * here should never contain dependency with version.<b>
 * 
 * </br></br>In order to be able to maintain this file easily, please add only
 * required dependencies, and make sure they are used. If any dependency is not
 * used any more, make sure it is removed from this file and pom file.
 * 
 * @author edejket
 * 
 */
public class DeploymentTestArtifact {

    /**
     * Camel engine rar artifacts needed for this test
     */
    public static final String COM_ERICSSON_NMS_MEDIATION_CAMEL_ENGINE_RAR = "com.ericsson.nms.mediation:camel-engine-jca-rar:rar";

    /**
     * Maven resolver that will try to resolve dependencies using pom.xml of the
     * project where this class is located.
     * 
     * @return MavenDependencyResolver
     */
    public static MavenDependencyResolver getMavenResolver() {
        return DependencyResolvers.use(MavenDependencyResolver.class)
                .loadMetadataFromPom("pom.xml");

    }

    /**
     * Resolve artifacts without dependencies
     * 
     * @param artifactCoordinates
     * @return
     */
    public static File resolveArtifactWithoutDependencies(
            final String artifactCoordinates) {
        final File[] artifacts = getMavenResolver()
                .artifact(artifactCoordinates).exclusion("*").resolveAsFiles();
        if (artifacts == null) {
            throw new IllegalStateException("Artifact with coordinates "
                    + artifactCoordinates + " was not resolved");
        }
        if (artifacts.length != 1) {
            throw new IllegalStateException(
                    "Resolved more then one artifact with coordinates "
                            + artifactCoordinates);
        }
        return artifacts[0];
    }

}
