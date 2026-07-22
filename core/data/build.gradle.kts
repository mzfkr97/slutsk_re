plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.built.in1.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.romanzhurid.data"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "PREF_PACKAGE_NAME", "\"app_prefs\"")
        buildConfigField("String", "PREF_IS_FIRST_APP_START", "\"pref_is_first_app_start\"")
        buildConfigField("String", "PREF_IS_DARK_THEME", "\"pref_is_dark_theme\"")
        buildConfigField("String", "YANDEX_BASE_API", "\"https://api.rasp.yandex.net/\"")
        buildConfigField("String", "YANDEX_MAP_API_KEY", "\"d1b6393c-5711-4d92-af7a-feefe51fa57a\"")
        buildConfigField("String", "CINEMA_URL", "\"https://api.megamag.by\"")
        buildConfigField("String", "CINEMA_API_KEY", "\"bO5qn2poZnN12K6PO1GIjFukaCTau5nP\"")
        buildConfigField("String", "BASE_URL_WHEATHER", "\"http://api.openweathermap.org/data/2.5/\"")
        buildConfigField("String", "KEY_WEATHER_API", "\"7893b0fde7d34a64a7706039929369ce\"")
        buildConfigField("String", "CURRENCY_URL", "\"https://www.nbrb.by/\"")
        buildConfigField("String", "BELARUSBANK_URL", "\"http://belarusbank.by/\"")
        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
    }

    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core:domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.security.crypto)

    api(libs.retrofit)
    api(libs.retrofit.kotlinx.serialization.converter)
    api(libs.okhttp.logging.interceptor)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    api(libs.kotlinx.serialization.json)
}