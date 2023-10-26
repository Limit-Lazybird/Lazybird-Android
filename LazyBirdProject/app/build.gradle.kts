import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
    id(libs.plugins.dagger.hilt.android.get().pluginId) // dagger hilt
    id(libs.plugins.androidx.navigation.get().pluginId) // navigation safeargs
    alias(libs.plugins.secrets.gradle)
}

android {
    defaultConfig {
        applicationId = "com.limit.lazybird"
        minSdk = 29
        targetSdk = 33
        compileSdk = 33
        versionCode = 12
        versionName = "1.0.11"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        val keyProperties = Properties().apply { load(file("${project.rootDir}/key.properties").inputStream()) }
        buildConfigField("String", "GOOGLE_SERVER_CLIENT_ID", "\"${keyProperties.getProperty("GOOGLE_SERVER_CLIENT_ID")}\"")
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${keyProperties.getProperty("KAKAO_NATIVE_APP_KEY")}\"")
        buildConfigField("String", "KAKAO_OAUTH_SCHEME", "\"${keyProperties.getProperty("KAKAO_OAUTH_SCHEME")}\"")
        buildConfigField("String", "LAZYBIRD_SERVER_URL", "\"${keyProperties.getProperty("LAZYBIRD_SERVER_URL")}\"")
        manifestPlaceholders += "KAKAO_OAUTH_SCHEME" to "\"${keyProperties.getProperty("KAKAO_OAUTH_SCHEME")}\""
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "server-version"
    productFlavors {
        create("real") {
            applicationIdSuffix = ".real"
            versionNameSuffix = ".real"
            buildConfigField("boolean", "IS_DEV_SERVER", "false")
        }
        create("dev") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = ".dev"
            buildConfigField("boolean", "IS_DEV_SERVER", "true")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true // for CalendarView
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(libs.jetbrains.kotlin)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.legacy.support)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // CalendarView
    implementation(libs.calendarview)
    coreLibraryDesugaring(libs.desugar)

    // Dagger hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // DataStore
    implementation(libs.androidx.datastore)

    // Flexbox
    implementation(libs.flexbox)

    // Fragment for fragmentResult
    implementation(libs.androidx.fragment)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Google login
    implementation(libs.google.playstore)

    // Kakao login
    implementation(libs.kakao.sdk)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.lifecycle.liveData)

    // Navigation fragment
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // ViewPager
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.viewpager2)

}
