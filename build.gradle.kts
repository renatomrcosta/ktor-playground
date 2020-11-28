plugins {
    kotlin("jvm") version "1.3.72"
}

group = "com.xunfos"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
    maven {
        setUrl("https://kotlin.bintray.com/ktor")
    }
}

configure<SourceSetContainer> {
    named("main") {
        java.srcDir("src/main/gen-java")
    }
}


dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8")
    implementation("io.ktor:ktor-server-netty:1.3.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-server-core:1.4.2")
    implementation("io.ktor:ktor-jackson:1.3.2")
    implementation("org.apache.thrift:libthrift:0.13.0")
}
