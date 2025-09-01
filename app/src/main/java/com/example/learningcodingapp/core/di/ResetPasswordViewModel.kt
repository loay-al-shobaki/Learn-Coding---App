package com.example.learningcodingapp.core.di

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningcodingapp.data.auth.AuthRepository
import com.example.learningcodingapp.data.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// ui/reset/ResetPasswordViewModel.kt
@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val sent: Boolean = false,
        val error: String? = null
    )

    var state by mutableStateOf(UiState())
        private set

    fun send(rawEmail: String) = viewModelScope.launch {
        val email = rawEmail.trim()
        if (!androidx.core.util.PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            state = state.copy(error = "Invalid email format"); return@launch
        }
        state = UiState(loading = true)
        when (val r = repo.sendPasswordReset(email)) {
            is AuthResult.Success -> state = UiState(sent = true)
            is AuthResult.Error   -> state = UiState(error = r.message)
        }
    }

    fun clearError() { state = state.copy(error = null) }
}
