package com.aspiring_creators.aichopaicho.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspiring_creators.aichopaicho.data.local.ScreenViewRepository
import com.aspiring_creators.aichopaicho.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PermissionViewModel @Inject constructor(
   val screenViewRepository: ScreenViewRepository
) : ViewModel(){

    fun markPermissionScreenShown()
    {
        viewModelScope.launch {
        screenViewRepository.markScreenAsShown(Routes.PERMISSION_CONTACTS_SCREEN)
        }
    }


}