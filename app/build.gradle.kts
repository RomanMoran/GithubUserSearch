import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.githubusersearch"
    compileSdk = 34

    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.githubusersearch"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        val key: String = gradleLocalProperties(rootDir).getProperty("github_api_token")
        buildConfigField("String", "GITHUB_API_TOKEN", "\"${key}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.navigation:navigation-compose:2.8.0-alpha05")

    implementation("io.insert-koin:koin-android:3.2.3")
    implementation("io.coil-kt:coil-compose:2.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":domain")))

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.9.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0") // Check for the latest version

    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    testImplementation("io.insert-koin:koin-test:3.1.2")

}