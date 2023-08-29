plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "ru.shtp"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jgit", "org.eclipse.jgit", "3.5.0.201409260305-r")
    implementation("com.google.code.gson", "gson", "2.10.1")
}


kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("ru.shtp.androidbuilder.MainKt")
}