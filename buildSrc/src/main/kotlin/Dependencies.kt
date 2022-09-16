object Deps {
    object AndroidX {
        val core by lazy { "androidx.core:core-ktx:${Versions.core}" }
        val appCompat by lazy { "androidx.appcompat:appcompat:${Versions.appCompat}" }
        val constraintLayout by lazy { "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}" }
        val recyclerView by lazy { "androidx.recyclerview:recyclerview:${Versions.recyclerView}" }

        object Lifecycle {
            val viewModel by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}" }
            val runtime by lazy { "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}" }
            val savedState by lazy { "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}" }
        }
    }

    object Jetpack {
        val activity by lazy { "androidx.activity:activity-ktx:${Versions.activity}" }
        val fragment by lazy { "androidx.fragment:fragment-ktx:${Versions.fragment}" }

        object Navigation {
            val gradlePlugin by lazy { "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}" }
            val fragment by lazy { "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}" }
            val ui by lazy { "androidx.navigation:navigation-ui-ktx:${Versions.navigation}" }
        }

        val paging by lazy { "androidx.paging:paging-runtime:${Versions.paging}" }
    }

    object Google {
        val material by lazy { "com.google.android.material:material:${Versions.material}" }

        object Hilt {
            val gradlePlugin by lazy { "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}" }
            val android by lazy { "com.google.dagger:hilt-android:${Versions.hilt}" }
            val compiler by lazy { "com.google.dagger:hilt-android-compiler:${Versions.hilt}" }
        }
    }

    object Square {
        object OkHttp {
            val okhttp by lazy { "com.squareup.okhttp3:okhttp:${Versions.okhttp}" }
            val loggingInterceptor by lazy { "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}" }
        }

        object Retrofit {
            val retrofit by lazy { "com.squareup.retrofit2:retrofit:${Versions.retrofit}" }
            val moshiConverter by lazy { "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}" }
        }

        val moshi by lazy { "com.squareup.moshi:moshi-kotlin:${Versions.moshi}" }
    }

    object Coroutines {
        val core by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}" }
        val android by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}" }
    }

    val coil by lazy { "io.coil-kt:coil:${Versions.coil}" }
    val timber by lazy { "com.jakewharton.timber:timber:${Versions.timber}" }
    val relinker by lazy { "com.getkeepsafe.relinker:relinker:${Versions.relinker}" }
}

object Test {
    val junit by lazy { "junit:junit:${Versions.jUnit}" }
    val junitExt by lazy { "androidx.test.ext:junit:${Versions.jUnitExt}" }
    val espresso by lazy { "androidx.test.espresso:espresso-core:${Versions.espresso}" }
}
