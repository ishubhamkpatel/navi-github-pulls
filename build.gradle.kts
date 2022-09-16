// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version Versions.gradlePlugin apply false
    id("com.android.library") version "7.2.2" apply false
    kotlin("android") version "1.7.10" apply false
}

buildscript {
    dependencies {
        classpath(Deps.Google.Hilt.gradlePlugin)
        classpath(Deps.Jetpack.Navigation.gradlePlugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
