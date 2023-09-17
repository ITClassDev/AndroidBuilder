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
    implementation("log4j", "log4j", "1.2.17")
    implementation("commons-io", "commons-io", "2.13.0")

}

application {
    mainClass.set("ru.shtp.androidbuilder.MainKt")
}