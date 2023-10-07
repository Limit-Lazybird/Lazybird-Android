import java.util.*

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin") // dagger hilt
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.secrets_gradle_plugin") version "0.6.1"
    id("androidx.navigation.safeargs.kotlin") // navigation safeargs
}

android {
    defaultConfig {
        applicationId = "com.limit.lazybird"
        minSdk = Version.Android.MinSdk
        targetSdk = Version.Android.TargetSdk
        compileSdk = Version.Android.CompileSdk
        versionCode = Version.Code
        versionName = Version.Name
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
    implementation(Library.Kotlin)
    implementation(Library.Core)
    implementation(Library.AppCompat)
    implementation(Library.Material)
    implementation(Library.ConstraintLayout)
    implementation(Library.LegacySupport)
    testImplementation(Library.Junit)
    androidTestImplementation(Library.JunitExt)
    androidTestImplementation(Library.EspressoCore)

    // CalendarView
    implementation(Library.Calendarview)
    coreLibraryDesugaring(Library.Desugar)

    // Dagger hilt
    implementation(Library.HiltAndroid)
    kapt(Library.HiltCompiler)

    // DataStore
    implementation(Library.Datastore)

    // Flexbox
    implementation(Library.Flexbox)

    // Fragment for fragmentResult
    implementation(Library.Fragment)

    // Glide
    implementation(Library.Glide)
    annotationProcessor(Library.GlideCompiler)

    // Google login
    implementation(Library.GooglePlayStore)

    // Kakao login
    implementation(Library.KakaoSdk)

    // Lifecycle
    implementation(Library.LifecycleViewModel)
    implementation(Library.LifecycleLiveData)

    // Navigation fragment
    implementation(Library.NavigationFragment)
    implementation(Library.NavigationUi)

    // Retrofit
    implementation(Library.Retrofit)
    implementation(Library.RetrofitConverterGson)

    // ViewPager
    implementation(Library.Cardview)
    implementation(Library.Viewpager2)

}
