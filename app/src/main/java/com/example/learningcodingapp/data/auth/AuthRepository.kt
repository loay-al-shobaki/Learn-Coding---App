package com.example.learningcodingapp.data.auth

interface AuthRepository {
    suspend fun signUp(email: String, password: String): AuthResult<Unit>
    suspend fun signIn(email: String, password: String): AuthResult<Unit>
    suspend fun sendPasswordReset(email: String): AuthResult<Unit>
    fun isSignedIn(): Boolean
    fun signOut()
}

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T): AuthResult<T>()
    data class Error(val message: String): AuthResult<Nothing>()
}