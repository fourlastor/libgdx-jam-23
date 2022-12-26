import com.google.protobuf.gradle.id

plugins {
    id("java")
    kotlin("jvm") version "1.7.22"
    id("com.google.protobuf") version "0.9.1"
}

group = "io.github.fourlastor"
version = "1.0"

repositories {
    mavenCentral()
}

val protobufVersion = "3.21.12"
val grpcVersion = "1.51.1"
val grpcKotlinVersion = "1.3.0"

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
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}
