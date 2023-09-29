plugins {
    id 'com.android.application'
    id 'dagger.hilt.android.plugin' // dagger hilt
    id 'kotlin-android'
    id 'kotlin-parcelize'
    id 'kotlin-kapt'
    id 'com.google.secrets_gradle_plugin' version '0.6.1'
    id "androidx.navigation.safeargs.kotlin" // navigation safeargs
}

android {
    defaultConfig {
        applicationId "com.limit.lazybird"
        minSdkVersion 29
        targetSdkVersion 33
        compileSdk 33
        versionCode 12
        versionName "1.0.11"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        def keyProperties = new Properties()
        keyProperties.load(project.rootProject.file("key.properties").newDataInputStream())
        buildConfigField "String", "GOOGLE_SERVER_CLIENT_ID", "\"${keyProperties.getProperty("GOOGLE_SERVER_CLIENT_ID")}\""
        buildConfigField "String", "KAKAO_NATIVE_APP_KEY", "\"${keyProperties.getProperty("KAKAO_NATIVE_APP_KEY")}\""
        buildConfigField "String", "KAKAO_OAUTH_SCHEME", "\"${keyProperties.getProperty("KAKAO_OAUTH_SCHEME")}\""
        buildConfigField "String", "LAZYBIRD_SERVER_URL", "\"${keyProperties.getProperty("LAZYBIRD_SERVER_URL")}\""
        manifestPlaceholders = [KAKAO_OAUTH_SCHEME:"\"${keyProperties.getProperty("KAKAO_OAUTH_SCHEME")}\""]
    }

    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    flavorDimensions += "server-version"
    productFlavors {
        real {
            dimension "server-version"
            applicationIdSuffix ".real"
            versionNameSuffix "-real"
            buildConfigField "boolean", "IS_DEV_SERVER", "false"
        }
        dev {
            dimension "server-version"
            applicationIdSuffix ".dev"
            versionNameSuffix "-dev"
            buildConfigField "boolean", "IS_DEV_SERVER", "true"
        }
    }

    compileOptions {
        coreLibraryDesugaringEnabled true // for CalendarView
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    buildFeatures {
        viewBinding true
        dataBinding true
    }
    lintOptions {
        checkReleaseBuilds false
        abortOnError false
    }
}

dependencies {

    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.10.1'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    testImplementation 'junit:junit:'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    // CalendarView
    implementation "com.github.kizitonwose:CalendarView:$calendarview_version"
    coreLibraryDesugaring "com.android.tools:desugar_jdk_libs:$desugar_version"

    // Dagger hilt
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-compiler:$hilt_version"

    // DataStore
    implementation "androidx.datastore:datastore-preferences:$datastore_version"

    // Flexbox
    implementation "com.google.android.flexbox:flexbox:$flexbox_version"

    // Fragment for fragmentResult
    implementation "androidx.fragment:fragment-ktx:$fragment_version"

    // Glide
    implementation "com.github.bumptech.glide:glide:$glide_version"
    annotationProcessor "com.github.bumptech.glide:compiler:$glide_version"

    // Google login
    implementation "com.google.android.gms:play-services-auth:$google_ps_version"

    // Kakao login
    implementation "com.kakao.sdk:v2-user:$kakao_version"

    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"

    // Navigation fragment
    implementation "androidx.navigation:navigation-fragment-ktx:$navigation_version"
    implementation "androidx.navigation:navigation-ui-ktx:$navigation_version"

    // Retrofit
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"

    // ViewPager
    implementation "androidx.cardview:cardview:$cardview_version"
    implementation "androidx.viewpager2:viewpager2:$viewpager2_version"

}
