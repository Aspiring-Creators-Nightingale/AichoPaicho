package com.aspiring_creators.aichopaicho.viewmodel.data

sealed class WelcomeScreenUiEvent {

    object OnLoginClicked : WelcomeScreenUiEvent()
    object OnSkipClicked : WelcomeScreenUiEvent()
}