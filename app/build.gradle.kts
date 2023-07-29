import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled


//new dependency
plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id ("androidx.navigation.safeargs.kotlin")

}

android {
//    compileSdk 32
//
//    defaultConfig {
//        applicationId "com.project.meditationsoundmixture"
//        minSdk 21
//        targetSdk 32
//        versionCode 1
//        versionName "1.0"
//
//        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
//    }
    namespace = "com.example.meditationsoundmixture"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.meditationsoundmixture"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
//    viewBinding{
//        enabled=true
//    }
//    dexOptions {
//       // incremental true
//        javaMaxHeapSize "4g"
//       // preDexLibraries true
//        dexInProcess = true
//    }

//    buildTypes {
//        release {
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }

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
    buildFeatures{
        dataBinding =true
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.github.smarteist:autoimageslider:1.3.9")
    implementation ("io.insert-koin:koin-core:3.1.6")
    implementation ("io.insert-koin:koin-android:3.1.6")
// dependency for loading image from url
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
    implementation  ("androidx.media:media:1.6.0")
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation ("io.github.luizgrp.sectionedrecyclerviewadapter:sectionedrecyclerviewadapter:3.2.0")
    //room
    implementation ("androidx.room:room-runtime:2.4.0-beta01")
    annotationProcessor ("androidx.room:room-compiler:2.4.0-beta01")
    kapt ("androidx.room:room-compiler:2.4.0-beta01")
    implementation ("androidx.room:room-ktx:2.4.0-beta01")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.work:work-runtime-ktx:2.7.1")

    //retrofit_moshi
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation ("com.squareup.moshi:moshi-adapters:1.12.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
    //work manager
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("com.airbnb.android:lottie:5.2.0")

    implementation("com.google.android.play:core-ktx:1.8.1")
    // Preferences DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0-alpha04")
    //smile rating
    implementation ("com.github.sujithkanna:smileyrating:2.0.0")



}