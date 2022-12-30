import com.google.protobuf.gradle.id

@Suppress(
    // known false positive: https://github.com/gradle/gradle/issues/22797
    "DSL_SCOPE_VIOLATION"
)
plugins {
    id("java")
    application
    alias(libs.plugins.kotlin.jvm) version libs.versions.kotlin
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.protobuf)
}

application {
    mainClass.set("io.github.fourlastor.MainKt")
}

group = "io.github.fourlastor"
version = "1.0"

repositories {
    mavenCentral()
}

val assetsDir = rootProject.files("assets")

sourceSets.main.configure {
    resources.srcDir(assetsDir)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpcKotlin.get()}:jdk8@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":core"))
    implementation(libs.grpc)
    implementation(libs.grpcKotlin)
    implementation(libs.grpcNetty)
    implementation(libs.protobufKotlin)
    implementation(libs.kotlinCoroutines)
    implementation(libs.gdxBackendHeadless)
    implementation(libs.dagger)
    implementation("com.badlogicgames.gdx:gdx-platform:${libs.versions.gdx.get()}:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:${libs.versions.gdx.get()}:natives-desktop")
    kapt(libs.daggerCompiler)
}
