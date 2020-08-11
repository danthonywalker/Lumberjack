/*
 * This file is part of Lumberjack.
 *
 * Lumberjack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lumberjack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Lumberjack.  If not, see <https://www.gnu.org/licenses/>.
 */
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("multiplatform") version "1.3.72"

    id("com.github.ben-manes.versions") version "0.29.0"

    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.5"
}

repositories {
    mavenCentral()
}

group = "dev.neontech.lumberjack"
val artifact: String = name.toLowerCase()
version = "0.3.2"

fun KotlinDependencyHandler.kotlinx(simpleModuleName: String, version: String? = null): String =
    "org.jetbrains.kotlinx:kotlinx-$simpleModuleName${if (version == null) "" else ":$version"}"

kotlin {
    jvm()
    js()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api(kotlinx("coroutines-core", "1.3.8"))
                api("org.apache.logging.log4j:log4j-api:2.13.3")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        val sawtoothMain by creating {
            dependsOn(commonMain)
        }
        val sawtoothTest by creating {
            dependsOn(commonTest)
        }

        val jsMain by getting {
            dependsOn(sawtoothMain)

            dependencies {
                api(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting {
            dependsOn(sawtoothTest)

            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")

    setPublications(*publishing.publications.names.toTypedArray())

    pkg.repo = "maven"
    pkg.name = artifact
    pkg.userOrg = "neontech"
    pkg.desc = "Kotlin Coroutine Multiplatform Logging Library"
    pkg.websiteUrl = "https://lumberjack.neontech.dev"
    pkg.issueTrackerUrl = "https://github.com/NeonTech/Lumberjack/issues"
    pkg.vcsUrl = "https://github.com/NeonTech/Lumberjack.git"
    pkg.setLicenses("LGPL-3.0")
    pkg.publicDownloadNumbers = true

    pkg.githubRepo = "NeonTech/Lumberjack"
    pkg.githubReleaseNotesFile = "README.md"

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    val released = ZonedDateTime.now(ZoneOffset.UTC).format(formatter)

    pkg.version.name = version.toString()
    pkg.version.released = released
    pkg.version.vcsTag = version.toString()
}

publishing {
    publications {
        withType<MavenPublication> {
            all {
                artifactId = when (name) {
                    "metadata" -> "$artifact-common"
                    else -> artifactId.toLowerCase()
                }
            }
        }
    }
}

tasks {
    dependencyUpdates {
        gradleReleaseChannel = "current"
    }

    wrapper {
        gradleVersion = "6.6"
        distributionType = Wrapper.DistributionType.ALL
    }
}
