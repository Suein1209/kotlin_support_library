buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.8.10")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.7")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
        classpath(kotlin("gradle-plugin", version = "1.8.10"))
        classpath(kotlin("serialization", version = "1.8.10"))
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        maven("https://maven.google.com")
        mavenCentral()
        gradlePluginPortal()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}