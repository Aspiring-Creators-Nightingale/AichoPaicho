package com.aspiring_creators.aichopaicho.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspiring_creators.aichopaicho.data.entity.User
import com.aspiring_creators.aichopaicho.data.repository.UserRepository
import com.aspiring_creators.aichopaicho.viewmodel.data.DashboardScreenUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardScreenUiState())
    val uiState: StateFlow<DashboardScreenUiState> = _uiState.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        viewModelScope.launch {
            val isSignedIn = auth.currentUser != null
            if (isSignedIn) {
            if(_uiState.value.user?.isOffline == false && getUserId() == auth.currentUser?.uid) {
                _uiState.value = _uiState.value.copy(isSignedIn = isSignedIn)
            }
            } else {
                _uiState.value = _uiState.value.copy(
                    user = null,
                    errorMessage = "Google backup not enabled"
                )
            }
        }
    }

    init {
       loadUserData()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    private fun setLoading(value: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = value)
    }

    private fun setErrorMessage(value: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = value)
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                setLoading(true)
                setErrorMessage(null)

                val user = userRepository.getUser()

                _uiState.value = _uiState.value.copy(
                    user = user,
                    isSignedIn = true
                )
                Log.d("DashboardViewModel", "User data loaded: ${user.name}")
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error loading user data", e)
                setErrorMessage("Failed to load user data: ${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    suspend fun signOut(): Result<Unit> {
        return try {
            setLoading(true)
            setErrorMessage(null)

            firebaseAuth.signOut()
            _uiState.value = DashboardScreenUiState() // Reset to initial state

            Log.d("DashboardViewModel", "User signed out successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("DashboardViewModel", "Error signing out", e)
            setErrorMessage("Failed to sign out: ${e.message}")
            Result.failure(e)
        } finally {
            setLoading(false)
        }
    }

    suspend fun refreshUserData(): Result<Unit> {

        return TODO("No need for refresh for now")
    }

    fun clearError() {
        setErrorMessage(null)
    }

    // Helper methods for easy access
    fun getUserName(): String? = uiState.value.user?.name
    fun getUserEmail(): String? = uiState.value.user?.email
    fun getUserId(): String? = uiState.value.user?.id
    fun isUserSignedIn(): Boolean = uiState.value.isSignedIn && uiState.value.user != null
}