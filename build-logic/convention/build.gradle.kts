plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("uiLibraryConvention") {
            id = "ui.library.convention"
            implementationClass = "UiLibraryConventionPlugin"
        }
    }
}
