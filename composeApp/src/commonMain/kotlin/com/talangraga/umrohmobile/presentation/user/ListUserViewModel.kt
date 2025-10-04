package com.talangraga.umrohmobile.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.umrohmobile.domain.repository.Repository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ListUserViewModel(
    private val repository: Repository
) : ViewModel() {

    init {
        getListUser()
    }

    fun getListUser() {
        repository.getListUsers()
            .onEach {

            }.launchIn(viewModelScope)
    }

}