package com.example.earntaka.Models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    val image: String? = null // image URL or Base64
)

@Serializable
data class UserProfile(
    val id: String? = null,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val createdAt: String? = null,
    val image: String? = null
)
