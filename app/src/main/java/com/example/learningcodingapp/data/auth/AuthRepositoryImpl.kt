package com.example.learningcodingapp.data.auth


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): AuthResult<Unit> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        AuthResult.Success(Unit)
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AuthResult.Error("Invalid email or weak password")
    } catch (e: Exception) {
        AuthResult.Error(e.message ?: "Unknown error")
    }


    override suspend fun signIn(email: String, password: String): AuthResult<Unit> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        AuthResult.Success(Unit)
    } catch (e: FirebaseAuthInvalidUserException) {
        AuthResult.Error("No account found for this email")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AuthResult.Error("Wrong email or password")
    } catch (e: Exception) {
        AuthResult.Error(e.message ?: "Unknown error")
    }

    override suspend fun sendPasswordReset(email: String): AuthResult<Unit> = try {
        auth.sendPasswordResetEmail(email.trim()).await()
        AuthResult.Success(Unit)
    } catch (e: FirebaseAuthInvalidUserException) {
        AuthResult.Error("No account found for this email")
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        AuthResult.Error("Invalid email address")
    } catch (e: com.google.firebase.FirebaseTooManyRequestsException) {
        AuthResult.Error("Too many attempts. Try again later")
    } catch (e: Exception) {
        AuthResult.Error(e.message ?: "Unknown error")
    }


    override fun isSignedIn() = auth.currentUser != null
    override fun signOut() = auth.signOut()
}
