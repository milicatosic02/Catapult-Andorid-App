package com.example.projekat45.networking.serialization

import kotlinx.serialization.json.Json

val AppJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = true
}