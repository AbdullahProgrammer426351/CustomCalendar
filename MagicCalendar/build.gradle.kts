plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply true // 👈 Add this
    id("maven-publish")

}

android {
    namespace = "com.custom.magic.calendar"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.0" // Use the correct Compose compiler
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.material3.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Accompanist Pager for Jetpack Compose
    implementation("com.google.accompanist:accompanist-pager:0.33.2-alpha")

    // Jetpack Compose Dependencies (Fixed Versions)
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.material:material:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")


    // locale date for prev versions
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.8")
}

publishing {
    publications {
        register<MavenPublication>("release") { // ✅ Correct Kotlin DSL syntax
            afterEvaluate{
                from(components["release"])
//                groupId = "com.custom.magic.calendar"
//                artifactId = "magic-calendar"
//                version = "1.0"
            }
        }
    }
}
