package com.aspiring_creators.aichopaicho.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspiring_creators.aichopaicho.data.repository.UserRepository
import com.aspiring_creators.aichopaicho.viewmodel.data.WelcomeScreenUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
   private val userRepository: UserRepository,
   private val firebaseAuth: FirebaseAuth
) : ViewModel(){

    private val _userUiState = MutableStateFlow(WelcomeScreenUiState())
    val userUiState: StateFlow<WelcomeScreenUiState> = _userUiState.asStateFlow()

    init {
        setUser()
    }

   private fun setUser()
    {
        viewModelScope.launch {
            val user = userRepository.getUser(firebaseAuth.currentUser!!.uid)
            _userUiState.value = WelcomeScreenUiState(user = user)
        }
    }

    fun getUserName(): String?
    {
        return userUiState.value.user?.name
    }


}