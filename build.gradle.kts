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
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("multiplatform") version "1.4.10"

    id("com.github.ben-manes.versions") version "0.31.0"

    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.5"
}

repositories {
    mavenCentral()
}

group = "dev.neontech.lumberjack"
val artifact: String = name.toLowerCase()
version = "0.7.0"

fun KotlinDependencyHandler.kotlinx(simpleModuleName: String, version: String? = null): String =
    "org.jetbrains.kotlinx:kotlinx-$simpleModuleName${if (version == null) "" else ":$version"}"

val ideaActive: Boolean = System.getProperty("idea.active")?.toBoolean() ?: false
val osName: String = System.getProperty("os.name")

kotlin {
    val metadata = metadata()   // Kotlin/Common
    val jvm = jvm()             // Kotlin/JVM

    val js = js {               // Kotlin/JS
        browser()
        nodejs()
    }

    // TODO android()           // Android
    androidNativeArm32()        // Android NDK
    androidNativeArm64()        // Android NDK
    iosArm32()                  // iOS
    iosArm64()                  // iOS
    iosX64()                    // iOS
    watchosArm32()              // watchOS
    watchosArm64()              // watchOS
    watchosX86()                // watchOS
    tvosArm64()                 // tvOS
    tvosX64()                   // tvOS
    linuxArm64()                // Linux
    linuxArm32Hfp()             // Linux
    linuxMips32()               // Linux
    linuxMipsel32()             // Linux
    linuxX64()                  // Linux
    macosX64()                  // MacOS
    mingwX64()                  // Windows
    mingwX86()                  // Windows
    val wasm32 = wasm32()       // WebAssembly

    if (!ideaActive) {
        val publishingTargets = when {
            "Linux" in osName -> {
                val universalTargets = setOf(jvm, js, wasm32)
                val androidTargets = targets.filter { it.name.startsWith("android") }
                val linuxTargets = targets.filter { it.name.startsWith("linux") }
                universalTargets + androidTargets + linuxTargets + metadata
            }

            "Mac" in osName -> {
                val iosTargets = targets.filter { it.name.startsWith("ios") }
                val watchosTargets = targets.filter { it.name.startsWith("watchos") }
                val tvosTargets = targets.filter { it.name.startsWith("tvos") }
                val macosTargets = targets.filter { it.name.startsWith("macos") }
                iosTargets + watchosTargets + tvosTargets + macosTargets + metadata
            }

            "Windows" in osName -> {
                val mingwTargets = targets.filter { it.name.startsWith("mingw") }
                mingwTargets + metadata
            }

            else -> TODO(osName)
        }

        val ignoredTargets = targets.filterNot(publishingTargets::contains)

        tasks.filter { task ->
            // Disable compile/publication tasks for ignored targets
            ignoredTargets.any { task.name.contains(it.name, true) }
        }.forEach { it.enabled = false }
    }

    sourceSets {
        val commonMain by getting {
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api(kotlinx("coroutines-core", "1.3.9"))
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
        }
        val jsTest by getting {
            dependsOn(sawtoothTest)

            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val nativeMain = maybeCreate("nativeMain")
        val nativeTest = maybeCreate("nativeTest")

        nativeMain.dependsOn(sawtoothMain)
        nativeMain.dependsOn(sawtoothTest)

        val nonNativeMains = setOf(commonMain, jvmMain, sawtoothMain, jsMain, nativeMain)
        val nonNativeTests = setOf(commonTest, jvmTest, sawtoothTest, jsTest, nativeTest)

        filterNot(nonNativeMains::contains)
            .filter { it.name.endsWith("Main") }
            .forEach { it.dependsOn(nativeMain) }
        filterNot(nonNativeTests::contains)
            .filter { it.name.endsWith("Test") }
            .forEach { it.dependsOn(nativeTest) }
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
            artifactId = when (name) {
                "metadata" -> "$artifact-common"
                else -> artifactId.toLowerCase()
            }
        }
    }
}

tasks {
    bintrayUpload {
        doFirst {
            publishing {
                publications {
                    withType<MavenPublication> {
                        // https://github.com/bintray/gradle-bintray-plugin/issues/229
                        val moduleFile = buildDir.resolve("publications/$name/module.json")
                        if (moduleFile.exists()) {
                            artifact(object : FileBasedMavenArtifact(moduleFile) {
                                override fun getDefaultExtension(): String = "module"
                            })
                        }
                    }
                }
            }
        }
    }

    dependencyUpdates {
        gradleReleaseChannel = "current"
    }

    wrapper {
        gradleVersion = "6.6.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}
