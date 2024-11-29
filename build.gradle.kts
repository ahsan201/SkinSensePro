// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false // Using plugin alias for Android
    id("com.google.gms.google-services") version "4.4.2" apply false // Firebase Google Services Plugin
}

buildscript {
    repositories {
        google() // Required for Firebase
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.2") // Firebase Google Services Plugin
    }
}
