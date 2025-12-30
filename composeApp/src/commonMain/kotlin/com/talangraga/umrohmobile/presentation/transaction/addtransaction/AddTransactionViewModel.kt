package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class AddTransactionViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserUIData>>(emptyList())
    val users = _users.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getListUser()
    }

    fun getListUser() {
        repository.getLocalUsers().onEach { result ->
            when (result) {
                is Result.Error -> {
                    _errorMessage.update { result.t.message }
                }
                is Result.Success -> {
                    val data = result.data.map { it.toUiData() }
                    _users.update { data }
                }
            }
        }.launchIn(viewModelScope)
    }

}
