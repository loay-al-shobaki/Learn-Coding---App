package com.example.learningcodingapp.core.di

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.learningcodingapp.data.auth.AuthRepository
import com.example.learningcodingapp.data.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(AuthUiState())
        private set

    fun signUp(email: String, password: String) = action {
        state = state.copy(loading = true, error = null, success = false)
        when (val res = repo.signUp(email, password)) {
            is AuthResult.Success -> state = AuthUiState(success = true)
            is AuthResult.Error   -> state = state.copy(loading = false, error = res.message)
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        state = state.copy(loading = true, error = null, success = false)
        when (val res = repo.signIn(email, password)) {
            is AuthResult.Success -> state = AuthUiState(success = true)   // ← مهم
            is AuthResult.Error   -> state = state.copy(loading = false, error = res.message)
        }
    }

    fun checkIfSignedIn() { if (repo.isSignedIn()) state = AuthUiState(success = true) }

    private fun action(block: suspend () -> Unit) = viewModelScope.launch { block() }
}

