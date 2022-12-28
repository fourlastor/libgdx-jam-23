import com.google.protobuf.gradle.id

@Suppress(
    // known false positive: https://github.com/gradle/gradle/issues/22797
    "DSL_SCOPE_VIOLATION"
)
plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm) version libs.versions.kotlin
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.protobuf)
}

group = "io.github.fourlastor"
version = "1.0"

repositories {
    mavenCentral()
}

val protobufVersion = "3.21.12"
val grpcVersion = "1.51.1"
val grpcKotlinVersion = "1.3.0"
val daggerVersion = "2.40"

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
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
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:${libs.versions.gdx.get()}:natives-desktop")
    kapt(libs.daggerCompiler)
}
