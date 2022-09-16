plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    compileSdk = Configs.compileSdkVersion

    defaultConfig {
        applicationId = "com.navi.git"
        minSdk = Configs.minSdkVersion
        targetSdk = Configs.targetSdkVersion
        versionCode = Configs.versionCode
        versionName = Configs.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(Modules.networking))
    implementation(project(Modules.img))
    implementation(project(Modules.logger))
    implementation(project(Modules.utils))

    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.constraintLayout)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.Lifecycle.viewModel)
    implementation(Deps.AndroidX.Lifecycle.runtime)
    implementation(Deps.AndroidX.Lifecycle.savedState)
    implementation(Deps.Jetpack.activity)
    implementation(Deps.Jetpack.fragment)
    implementation(Deps.Jetpack.Navigation.fragment)
    implementation(Deps.Jetpack.Navigation.ui)
    implementation(Deps.Jetpack.paging)
    implementation(Deps.Google.material)
    implementation(Deps.Google.Hilt.android)
    kapt(Deps.Google.Hilt.compiler)
    implementation(Deps.Coroutines.android)

    testImplementation(Test.junit)
    androidTestImplementation(Test.junitExt)
    androidTestImplementation(Test.espresso)
}

kapt {
    correctErrorTypes = true
}
