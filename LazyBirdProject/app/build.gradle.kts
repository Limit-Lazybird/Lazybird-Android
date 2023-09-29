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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // CalendarView
    implementation("com.github.kizitonwose:CalendarView:${rootProject.extra["calendarview_version"]}")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${rootProject.extra["desugar_version"]}")

    // Dagger hilt
    implementation("com.google.dagger:hilt-android:${rootProject.extra["hilt_version"]}")
    kapt("com.google.dagger:hilt-compiler:${rootProject.extra["hilt_version"]}")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:${rootProject.extra["datastore_version"]}")

    // Flexbox
    implementation("com.google.android.flexbox:flexbox:${rootProject.extra["flexbox_version"]}")

    // Fragment for fragmentResult
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["fragment_version"]}")

    // Glide
    implementation("com.github.bumptech.glide:glide:${rootProject.extra["glide_version"]}")
    annotationProcessor("com.github.bumptech.glide:compiler:${rootProject.extra["glide_version"]}")

    // Google login
    implementation("com.google.android.gms:play-services-auth:${rootProject.extra["google_ps_version"]}")

    // Kakao login
    implementation("com.kakao.sdk:v2-user:${rootProject.extra["kakao_version"]}")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")

    // Navigation fragment
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigation_version"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["navigation_version"]}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofit_version"]}")
    implementation("com.squareup.retrofit2:converter-gson:${rootProject.extra["retrofit_version"]}")

    // ViewPager
    implementation("androidx.cardview:cardview:${rootProject.extra["cardview_version"]}")
    implementation("androidx.viewpager2:viewpager2:${rootProject.extra["viewpager2_version"]}")

}
