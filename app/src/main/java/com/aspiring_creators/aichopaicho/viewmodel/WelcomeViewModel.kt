package com.aspiring_creators.aichopaicho.viewmodel

import android.content.Context
import android.credentials.GetCredentialException
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspiring_creators.aichopaicho.R
import com.aspiring_creators.aichopaicho.data.dto.UserDto
import com.aspiring_creators.aichopaicho.data.mapper.toUserEntity
import com.aspiring_creators.aichopaicho.data.repository.ScreenViewRepository
import com.aspiring_creators.aichopaicho.data.repository.UserRepository
import com.aspiring_creators.aichopaicho.ui.navigation.Routes
import com.aspiring_creators.aichopaicho.viewmodel.data.WelcomeScreenUiState
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeScreenUiState())
    val uiState: StateFlow<WelcomeScreenUiState> = _uiState.asStateFlow()

    fun setLoading(value: Boolean)
    {
        _uiState.value = _uiState.value.copy(isLoading = value)
    }
    fun setErrorMessage(value: String?)
    {
        _uiState.value = _uiState.value.copy(errorMessage = value)
    }
     fun setUser(firebaseUser: FirebaseUser?)
    {
        val user = firebaseUser?.toUserEntity()
        _uiState.value = _uiState.value.copy(user = user)
    }

    fun handleGoogleIdToken()
    {
        viewModelScope.launch {
            setLoading(true)
            try
            {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client))
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val credentialManager = CredentialManager.create(context)

                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                handleSignIn(result)

                // insert into database
                val user = uiState.value.user
                user?.let {
                    userRepository.upsert(user)
                }

            } catch (e: Exception) {
                setErrorMessage(e.message ?: "Sign in failed")
            } finally {
                setLoading(false)
    }
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse)
    {
        when(val credential = result.credential)
        {
            is GoogleIdTokenCredential -> {

                val tokenCredential =  GoogleIdTokenCredential.createFrom(credential.data)

                val idToken = tokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken,null)

                val authResult = firebaseAuth.signInWithCredential(firebaseCredential)
                    .await()
                setUser(authResult.user)
            }else -> setErrorMessage("Invalid Credential Type")
        }
    }

    suspend fun <T> Task<T>.await(): T {
        return suspendCancellableCoroutine { cont ->
            addOnCompleteListener { task ->
                if (task.exception != null) {
                    cont.resumeWithException(task.exception!!)
                } else {
                    cont.resume(task.result)
                }
            }
        }
    }

}