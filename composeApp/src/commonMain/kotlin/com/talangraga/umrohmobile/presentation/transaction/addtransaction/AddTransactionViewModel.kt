package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import io.github.aakira.napier.Napier
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

    private val _periods = MutableStateFlow<List<PeriodEntity>>(emptyList())
    val periods = _periods.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val selectedPeriod = mutableStateOf<PeriodEntity?>(null)

    init {
        getListUser()
        getPeriods()
        getListPayments()
    }

    fun setSelectedPeriod(period: PeriodEntity?) {
        selectedPeriod.value = period
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

    fun getPeriods() {
        repository.getPeriods()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        _periods.update { result.data }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun getListPayments() {
        repository.getPayments()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        val data = result.data
                        Napier.i { data.toString() }
                    }
                }
            }.launchIn(viewModelScope)
    }

}
