// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlin_version = "1.8.20"
    val hilt_version = "2.47"
    val navigation_version = "2.6.0"

    extra["calendarview_version"] = "1.0.4"
    extra["cardview_version"] = "1.0.0"
    extra["datastore_version"] = "1.0.0-alpha02"
    extra["desugar_version"] = "1.1.5"
    extra["flexbox_version"] = "3.0.0"
    extra["fragment_version"] = "1.6.1"
    extra["glide_version"] = "4.12.0"
    extra["google_ps_version"] = "19.2.0"
    extra["gson_version"] = "2.8.6"
    extra["hilt_version"] = hilt_version
    extra["kakao_version"] = "2.8.2"
    extra["kotlin_version"] = kotlin_version
    extra["lifecycle_version"] = "2.4.0"
    extra["navigation_version"] = navigation_version
    extra["retrofit_version"] = "2.9.0"
    extra["viewpager2_version"] = "1.0.0"
    extra["secrets_gradle_plugin"] = "2.0.0"

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version") // dagger hilt
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navigation_version")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
