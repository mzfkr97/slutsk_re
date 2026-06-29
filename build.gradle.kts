plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.built.in1.kotlin) apply false
    alias(libs.plugins.kotlin.server) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.detekt.plugin)
}

subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        dependencies {
            add(
                "detektPlugins",
                "io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8"
            )
        }
        detekt {
            source.setFrom(files("src/main/java", "src/main/kotlin"))
            config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
            buildUponDefaultConfig = true
            autoCorrect = true
        }
    }
}