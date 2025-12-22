import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

// Task to generate configuration from Env Vars (CI) or local properties (Dev)
val generateConfig = tasks.register("generateConfig") {
    // Check Env Vars first (standard for CI), then gradle properties
    val supabaseUrl = System.getenv("SUPABASE_URL") 
        ?: project.findProperty("SUPABASE_URL")?.toString() 
        ?: ""
    val supabaseKey = System.getenv("SUPABASE_ANON_KEY") 
        ?: project.findProperty("SUPABASE_ANON_KEY")?.toString() 
        ?: ""
        
    val outputDir = layout.buildDirectory.dir("generated/powertracker/commonMain/kotlin")
    
    inputs.property("url", supabaseUrl)
    inputs.property("key", supabaseKey)
    outputs.dir(outputDir)
    
    doLast {
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
