package com.example.learningcodingapp.data.UserProfile


import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
class UserRepository {
    private val auth = Firebase.auth

    suspend fun loadProfile(): UserProfile {
        val u = auth.currentUser ?: error("Not logged in")
        return UserProfile(
            uid = u.uid,
            displayName = u.displayName?.takeIf { it.isNotBlank() }
                ?: (u.email?.substringBefore("@") ?: "User"),
            email = u.email ?: "",
            photoUrl = u.photoUrl?.toString()
        )
    }

    suspend fun updateDisplayName(newName: String) {
        val u = auth.currentUser ?: error("Not logged in")
        u.updateProfile(userProfileChangeRequest { displayName = newName }).await()
        // مافي Firestore هنا
    }

    // لو ما عندك Storage، احذف هذه وخلّي الصورة عبارة عن رابط جاهز فقط
    // أو اتركها غير مستخدمة مؤقتاً.
}
