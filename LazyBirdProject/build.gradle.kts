
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    ext {
        calendarview_version = "1.0.4"
        cardview_version = "1.0.0"
        datastore_version = "1.0.0-alpha02"
        desugar_version = "1.1.5"
        flexbox_version = "3.0.0"
        fragment_version = "1.6.1"
        glide_version = "4.12.0"
        google_ps_version = "19.2.0"
        gson_version = "2.8.6"
        hilt_version = '2.47'
        kakao_version = "2.8.2"
        kotlin_version = '1.8.20'
        lifecycle_version = "2.4.0"
        navigation_version = "2.6.0"
        retrofit_version = "2.9.0"
        viewpager2_version = "1.0.0"
        secrets_gradle_plugin = "2.0.0"
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:7.4.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version" // dagger hilt
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version"
        classpath "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1"
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url "https://jitpack.io" }
        maven { url "https://devrepo.kakao.com/nexus/content/groups/public/" }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}