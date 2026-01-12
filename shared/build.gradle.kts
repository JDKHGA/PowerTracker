import java.util.Properties
import java.io.FileInputStream
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

abstract class GenerateConfigTask : org.gradle.api.DefaultTask() {

    @get:org.gradle.api.tasks.Optional
    @get:org.gradle.api.tasks.InputFile
    abstract val localProperties: org.gradle.api.file.RegularFileProperty

    @get:org.gradle.api.tasks.OutputDirectory
    abstract val outputDir: org.gradle.api.file.DirectoryProperty

    @TaskAction
    fun generate() {
        val props = Properties()
        val localFile = localProperties.orNull?.asFile
        if (localFile != null && localFile.exists()) {
            props.load(localFile.inputStream())
        }

        val supabaseUrl = System.getenv("SUPABASE_URL")
            ?: props.getProperty("supabase.url")
            ?: project.findProperty("SUPABASE_URL")?.toString()
            ?: ""
        val supabaseKey = System.getenv("SUPABASE_ANON_KEY")
            ?: props.getProperty("supabase.key")
            ?: project.findProperty("SUPABASE_ANON_KEY")?.toString()
            ?: ""

        val configFile = outputDir.get().file("com/example/powertracker/SupabaseConfig.kt").asFile
        configFile.parentFile.mkdirs()
        configFile.writeText("""
            package com.example.powertracker

            object SupabaseConfig {
                const val URL = "$supabaseUrl"
                const val KEY = "$supabaseKey"
            }
        """.trimIndent())
    }
}


// Task to generate configuration from Env Vars (CI) or local properties (Dev)
val generateConfig = tasks.register<GenerateConfigTask>("generateConfig") {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localProperties.set(localFile)
    }
    outputDir.set(layout.buildDirectory.dir("generated/powertracker/commonMain/kotlin"))
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    sourceSets {
        commonMain {
            kotlin.srcDir(generateConfig)

            dependencies {
                implementation(libs.kotlinx.serialization.json)
                api(libs.supabase.client)
                api(libs.supabase.auth)
                api(libs.supabase.postgrest)
                api(libs.supabase.functions)
                implementation(libs.ktor.client.core)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.powertracker.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
