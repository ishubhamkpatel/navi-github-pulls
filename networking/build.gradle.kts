plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Configs.compileSdkVersion

    defaultConfig {
        minSdk = Configs.minSdkVersion
        targetSdk = Configs.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(Modules.logger))
    implementation(project(Modules.utils))

    implementation(Deps.Jetpack.paging)
    implementation(Deps.Google.Hilt.android)
    kapt(Deps.Google.Hilt.compiler)
    implementation(Deps.Square.OkHttp.okhttp)
    implementation(Deps.Square.OkHttp.loggingInterceptor)
    implementation(Deps.Square.Retrofit.retrofit)
    implementation(Deps.Square.Retrofit.moshiConverter)
    implementation(Deps.Coroutines.core)

    testImplementation(Test.junit)
    androidTestImplementation(Test.junitExt)
    androidTestImplementation(Test.espresso)
}

kapt {
    correctErrorTypes = true
}