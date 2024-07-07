plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.ipostu.mybasicsample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ipostu.mybasicsample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] =
                    "$projectDir/schemas"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)

    implementation("androidx.room:room-runtime:2.4.3")
    annotationProcessor("androidx.room:room-compiler:2.4.3")

    implementation("androidx.lifecycle:lifecycle-runtime:2.5.1")
    annotationProcessor("androidx.lifecycle:lifecycle-compiler:2.5.1")
    runtimeOnly("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // Android Testing Support Library's runner and rules
    androidTestImplementation("androidx.test:core:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:runner:1.5.0")
    androidTestImplementation("androidx.test:rules:1.6.0")
    androidTestImplementation("androidx.room:room-testing:2.4.3")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")

    // Espresso UI Testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // Resolve conflicts between main and test APK:
    androidTestImplementation("androidx.annotation:annotation:1.2.0")
    androidTestImplementation(libs.androidx.appcompat)
    androidTestImplementation(libs.material)
}