plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdk = Apps.COMPILE_SDK

    defaultConfig {
        applicationId = "com.example.newcalendar"
        minSdk = Apps.MIN_SDK
        targetSdk = Apps.TARGET_SDK
        versionCode = Apps.VERSION_CODE
        versionName = Apps.VERSION_NAME
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release"){
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(KTX.CORE)

    implementation(AndroidX.APPCOMPAT)
    implementation(AndroidX.MATERIAL)
    implementation(AndroidX.CONSTRAINT_LAYOUT)

    testImplementation(Test.JUNIT)
    androidTestImplementation(AndroidTest.EXT_JUNIT)
    androidTestImplementation(AndroidTest.ESPRESSO_CORE)

    // Material CalendarView
    implementation(MaterialCalendarView.MATERIAL_CALENDARVIEW)

    // Navigation
    implementation(NavComponent.NAVIGATION_FRAGMENT)
    implementation(NavComponent.NAVIGATION_UI)
    implementation(NavComponent.NAVIGATION_DYNAMIC_FEATURES_FRAGMENT)
    implementation(NavComponent.NAVIGATION_TESTING)

    // DataStore
    implementation(DataStore.DATASTORE)
    implementation(DataStore.DATASTORE_CORE)

    // Koin
    implementation(Koin.KOIN)
    implementation(Koin.KOIN_COMPAT)

    // Room
    implementation(Room.ROOM)
    implementation(Room.ROOM_RUNTIME)
    kapt(Room.ROOM_COMPILER)

    // Lifecycle
    implementation(LifeCycle.LIFECYCLE_COMMON)
    implementation(LifeCycle.LIFECYCLE_VIEWMODEL)
    implementation(LifeCycle.LIFECYCLE_LIVEDATA)
    implementation(LifeCycle.LIFECYCLE_RUNTIME)

    // Toast
    implementation(Toast.FANCY_TOAST)
    implementation(Toast.STYLEABLE_TOAST)

    // BottomBar
    implementation(BottomBar.SMOOTH_BOTTOMBAR)

    // ViewBindingPropertyDelegate
    implementation(ViewBindingDelegate.VIEWBINDING_DELEGATE)
    implementation(ViewBindingDelegate.VIEWBINDING_DELEGATE_NO_REFLECTION)

    // LeakCanary
    debugImplementation(LeakCanary.LEAKCANARY)
}