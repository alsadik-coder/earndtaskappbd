package com.example.earntaka.Data

import com.example.earntaka.Models.User
import com.example.earntaka.Models.UserProfile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DataRepository {

    // ========== Authentication Operations ==========

    /**
     * Sign up a new user with email and password
     */
    suspend fun signUpUser(user: User): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            // Create auth user
            supabase.auth.signUpWith(Email) {
                email = user.email
                password = user.password
            }

            // Get the current session after signup
            val session = supabase.auth.currentSessionOrNull()
            val userId = session?.user?.id ?: throw Exception("User ID not found")

            // Create user profile in database
            val userProfile = UserProfile(
                id = userId,
                fullName = user.fullName,
                phoneNumber = user.phoneNumber,
                email = user.email,
                image = user.image
            )

            supabase.from("profiles").insert(userProfile)

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sign in existing user
     */
    suspend fun signInUser(email: String, password: String): Result<UserProfile> =
        withContext(Dispatchers.IO) {
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }

                val session = supabase.auth.currentSessionOrNull()
                val userId = session?.user?.id ?: throw Exception("User ID not found")
                val profile = getUserProfile(userId).getOrThrow()

                Result.success(profile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Sign out current user
     */
    suspend fun signOutUser(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get current authenticated user
     */
    suspend fun getCurrentUser(): Result<UserProfile?> = withContext(Dispatchers.IO) {
        try {
            val session = supabase.auth.currentSessionOrNull()
            if (session != null) {
                val userId = session.user?.id ?: throw Exception("User ID not found")
                val profile = getUserProfile(userId).getOrThrow()
                Result.success(profile)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reset password via email
     */
    suspend fun resetPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== Profile Operations ==========

    /**
     * Get user profile by ID
     */
    suspend fun getUserProfile(userId: String): Result<UserProfile> =
        withContext(Dispatchers.IO) {
            try {
                val profile = supabase.from("profiles")
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("id", userId)
                        }
                    }
                    .decodeSingle<UserProfile>()

                Result.success(profile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(userProfile: UserProfile): Result<UserProfile> =
        withContext(Dispatchers.IO) {
            try {
                supabase.from("profiles")
                    .update(
                        {
                            set("full_name", userProfile.fullName)
                            set("phone_number", userProfile.phoneNumber)
                            set("image", userProfile.image)
                        }
                    ) {
                        filter {
                            eq("id", userProfile.id!!)
                        }
                    }

                Result.success(userProfile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Delete user profile
     */
    suspend fun deleteUserProfile(userId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                supabase.from("profiles").delete {
                    filter {
                        eq("id", userId)
                    }
                }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // ========== Storage Operations ==========

    /**
     * Upload profile image to storage
     */
    suspend fun uploadProfileImage(userId: String, imageFile: File): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val bucket = supabase.storage.from("profile-images")
                val fileName = "$userId-${System.currentTimeMillis()}.jpg"

                bucket.upload(fileName, imageFile.readBytes())

                val imageUrl = bucket.publicUrl(fileName)
                Result.success(imageUrl)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Upload profile image from byte array
     */
    suspend fun uploadProfileImageBytes(userId: String, imageBytes: ByteArray): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val bucket = supabase.storage.from("profile-images")
                val fileName = "$userId-${System.currentTimeMillis()}.jpg"

                bucket.upload(fileName, imageBytes)

                val imageUrl = bucket.publicUrl(fileName)
                Result.success(imageUrl)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Delete profile image from storage
     */
    suspend fun deleteProfileImage(imageUrl: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                // Extract filename from URL
                val fileName = imageUrl.substringAfterLast("/")
                val bucket = supabase.storage.from("profile-images")

                bucket.delete(fileName)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    // ========== Session Management ==========

    /**
     * Check if user is authenticated
     */
    fun isUserAuthenticated(): Boolean {
        return supabase.auth.currentSessionOrNull() != null
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return supabase.auth.currentSessionOrNull()?.user?.id
    }
}