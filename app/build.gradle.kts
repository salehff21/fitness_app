plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.fitness_app"
    compileSdk = 35
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.fitness_app"
        minSdk = 26
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
}

dependencies {
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.2")
    implementation("androidx.health.connect:connect-client:1.1.0-alpha06")
    implementation ("androidx.sqlite:sqlite:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1") // اختياري
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}