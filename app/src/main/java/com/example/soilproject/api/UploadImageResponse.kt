package com.example.soilproject.api

data class UploadImageResponse(
        val error: Boolean,
        val message: String,
        val image: String?
)