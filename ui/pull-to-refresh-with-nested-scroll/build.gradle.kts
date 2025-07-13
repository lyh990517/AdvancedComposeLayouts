plugins {
    alias(libs.plugins.convention.compose)
}

android {
    namespace = "com.yunho.pull"
}

dependencies {
    implementation(project(":common"))
}