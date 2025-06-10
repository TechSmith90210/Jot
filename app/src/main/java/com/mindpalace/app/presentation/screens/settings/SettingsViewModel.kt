//package com.mindpalace.app.presentation.screens.settings
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.mindpalace.app.domain.usecase.SignOutUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class SettingsViewModel @Inject constructor(private val signOutUseCase: SignOutUseCase): ViewModel() {
//    fun signOut(){
//        viewModelScope.launch{
//            signOutUseCase.execute()
//        }
//
//    }
//}