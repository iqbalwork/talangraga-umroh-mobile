package com.talangraga.umrohmobile.presentation.user.editprofile

import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class EditProfileState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val user: UserUIData? = null,
    val isLoginUser: Boolean = false,
    val isMember: Boolean = false,
    val userId: Int = 0,
    val username: String = "",
    val fullname: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val domicile: String = "",
    val imageUrl: String = ""
)

sealed interface EditProfileEvent {
    data class InitScope(val userId: Int, val isLoginUser: Boolean) : EditProfileEvent
    data class OnUsernameChange(val value: String) : EditProfileEvent
    data class OnFullnameChange(val value: String) : EditProfileEvent
    data class OnPhoneNumberChange(val value: String) : EditProfileEvent
    data class OnEmailChange(val value: String) : EditProfileEvent
    data class OnDomicileChange(val value: String) : EditProfileEvent
    data class OnImageChange(val value: String) : EditProfileEvent
    data object ClearError : EditProfileEvent
    data object SaveProfile : EditProfileEvent
}

sealed interface EditProfileEffect {
    data class ShowToastError(val message: String) : EditProfileEffect
}
