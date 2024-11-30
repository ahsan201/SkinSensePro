plugins {
    alias(libs.plugins.android.application) // Use this if configured in libs.versions.toml
    id("com.google.gms.google-services") // Firebase plugin
}

android {
    namespace = "com.example.skinsensepro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.skinsensepro"
        minSdk = 25
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true // Enable View Binding
    }
}

dependencies {
    // Core dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // Firebase services
    implementation("com.google.firebase:firebase-auth") // Authentication
    implementation("com.google.firebase:firebase-firestore") // Firestore database
    implementation("com.google.firebase:firebase-storage")

    // Fragment UI
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("com.google.android.material:material:1.9.0") // Or latest version

    // Optional (for LiveData and ViewModel)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    // Optional (for Navigation)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")

    // Optional (for RecyclerView)
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // Bar code Implementation
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
}
