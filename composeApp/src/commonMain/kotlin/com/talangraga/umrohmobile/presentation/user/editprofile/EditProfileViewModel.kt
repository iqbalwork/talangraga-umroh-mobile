package com.talangraga.umrohmobile.presentation.user.editprofile

import androidx.compose.runtime.mutableStateOf
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
class EditProfileViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    private val _user = MutableStateFlow<UserUIData?>(null)
    val user = _user.asStateFlow()

    val isLoginUser = mutableStateOf(false)
    val userId = mutableStateOf(0)
    val fullname = mutableStateOf("")
    val phoneNumber = mutableStateOf("")
    val email = mutableStateOf("")
    val domicile = mutableStateOf("")
    val imageUrl = mutableStateOf("")

    fun onFullnameChange(value: String) {
        fullname.value = value
    }

    fun onPhoneNumberChange(value: String) {
        phoneNumber.value = value
    }

    fun onEmailChange(value: String) {
        email.value = value
    }

    fun onDomicileChange(value: String) {
        domicile.value = value
    }

    fun onImageChange(value: String) {
        imageUrl.value = value
    }

    fun onSaveClick() {
        // TODO: Implement save logic
        if (isLoginUser.value) {

        } else {

        }
    }

    fun getUser(userId: Int) {
        repository.getUser(userId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        val data = result.data.toUiData()
                        onFullnameChange(data.fullname)
                        onPhoneNumberChange(data.phone)
                        onEmailChange(data.email)
                        onDomicileChange(data.domicile)
                        onImageChange(data.imageProfileUrl)
                        _user.update { result.data.toUiData() }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun clearError() {
        _errorMessage.update { null }
    }

}
