package com.example.learningcodingapp.data.UserProfile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class ProfileUiState(
    val loading: Boolean = true,
    val profile: UserProfile? = null,
    val error: String? = null,
    val saving: Boolean = false
)

class ProfileViewModel(
    private val repo: UserRepository = UserRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init { refresh() }

    fun refresh() = viewModelScope.launch {
        _state.value = ProfileUiState(loading = true)
        try {
            val p = repo.loadProfile()
            _state.value = ProfileUiState(loading = false, profile = p)
        } catch (e: Exception) {
            _state.value = ProfileUiState(loading = false, error = e.message)
        }
    }

    fun updateName(newName: String) = viewModelScope.launch {
        val cur = _state.value
        _state.value = cur.copy(saving = true, error = null)
        try {
            repo.updateDisplayName(newName)
            refresh()
        } catch (e: Exception) {
            _state.value = cur.copy(saving = false, error = e.message)
        }
    }

//    fun updateAvatar(uri: Uri) = viewModelScope.launch {
//        val cur = _state.value
//        _state.value = cur.copy(saving = true, error = null)
//        try {
//            repo.uploadAvatarAndUpdate(uri)
//            refresh()
//        } catch (e: Exception) {
//            _state.value = cur.copy(saving = false, error = e.message)
//        }
//    }
}