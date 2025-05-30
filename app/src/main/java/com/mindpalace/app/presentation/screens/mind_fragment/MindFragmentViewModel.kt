package com.mindpalace.app.presentation.screens.mind_fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindpalace.app.domain.model.MindFragment
import com.mindpalace.app.domain.usecase.mind_fragment.MindFragmentUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MindFragmentViewModel @Inject constructor(
    private val useCases: MindFragmentUseCases
) : ViewModel() {

    private val _state = MutableStateFlow<MindFragmentState>(MindFragmentState.Idle)
    val state: StateFlow<MindFragmentState> = _state

    private val _fragment = MutableStateFlow<MindFragment?>(null)
    val fragment: StateFlow<MindFragment?> = _fragment.asStateFlow()


    fun createFragment(title: String = "Untitled Fragment") {
        val defaultContent = """
        {
          "title": "$title",
          "blocks": [
            {
              "id": "${UUID.randomUUID()}",
              "text": "blah blah blah"
            }
          ]
        }
    """.trimIndent()

        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.createFragment(title, defaultContent)
            _state.value = if (result.isSuccess) {
                val fragmentId = result.getOrNull() ?: ""
                MindFragmentState.Success(fragmentId = fragmentId)
            } else {
                MindFragmentState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }


    fun resetState() {
        _state.value = MindFragmentState.Idle
    }


    fun getFragmentsByLastOpened(userId: String, limit: Long) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.getFragmentsByLastOpened(userId, limit)
            if (result.isSuccess) {
                _state.value = MindFragmentState.Success()
            } else {
                _state.value =
                    MindFragmentState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun getFragmentsByCreatedAt(userId: String, limit: Long) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.getFragmentsByCreatedAt(userId, limit)
            if (result.isSuccess) {
                _state.value = MindFragmentState.SuccessList(result.getOrNull())
            } else {
                _state.value =
                    MindFragmentState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun getAllFragments(userId: String) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.getAllFragments(userId)
            if (result.isSuccess) {
                _state.value = MindFragmentState.SuccessList(result.getOrNull())
            } else {
                _state.value =
                    MindFragmentState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun loadFragments(userId: String) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            try {
                val createdList = useCases.getFragmentsByCreatedAt(userId, limit = 10)
                val lastOpenedList = useCases.getFragmentsByLastOpened(userId, limit = 10)
                _state.value = MindFragmentState.SuccessList(createdList= createdList.getOrNull(), lastOpenedList= lastOpenedList.getOrNull())
            } catch (e: Exception) {
                _state.value = MindFragmentState.Error(e.message ?: "Unknown error")
            }
        }
    }


    fun getFragmentById(fragmentId: String) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.getFragment(fragmentId)
            result.fold(
                onSuccess = { fragmentData ->
//                    Log.d("ViewModel", "Fragment loaded: ${fragmentData.toString()}")
//                    Log.d("MindFragmentEditor", "Fragment content JSON: ${fragmentData.content}")

                    _fragment.value = fragmentData
                    _state.value = MindFragmentState.Success()
                },
                onFailure = { e ->
                    Log.e("ViewModel", "Failed to load fragment: ${e.message}")
                    _state.value = MindFragmentState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }


    fun updateFragment(fragment: MindFragment) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.updateFragment(fragment)

            if (result.isSuccess) {
                _state.value = MindFragmentState.Success()
            } else {
                _state.value =
                    MindFragmentState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun deleteFragment(userId: String, fragmentId: String) {
        viewModelScope.launch {
            _state.value = MindFragmentState.Loading
            val result = useCases.deleteFragment(userId, fragmentId)

            if (result.isSuccess) {
                _state.value = MindFragmentState.Success(fragmentId = fragmentId)
            } else {
                _state.value =
                    MindFragmentState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
