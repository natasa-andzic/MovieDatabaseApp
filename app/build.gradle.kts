plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.natasaandzic.moviedatabase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.natasaandzic.moviedatabase"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

// Retrofit core
    implementation(libs.retrofit)

// Converter for JSON (Gson is common, you can also use Moshi)
    implementation(libs.converter.gson)

// OkHttp (used under the hood by Retrofit)
    implementation(libs.okhttp)

    implementation(libs.okhttp)

    // Hilt core
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

// Optional: Hilt Navigation Compose
    implementation(libs.androidx.hilt.navigation.compose)

// Required for ViewModel injection
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.coil.compose)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.accompanist.swiperefresh)

    implementation(libs.core)


    configurations.all {
        resolutionStrategy {
            force("androidx.navigation:navigation-compose:2.7.7")
        }
    }

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}