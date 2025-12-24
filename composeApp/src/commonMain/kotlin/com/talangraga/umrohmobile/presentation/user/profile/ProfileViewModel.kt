package com.talangraga.umrohmobile.presentation.user.profile

import androidx.lifecycle.ViewModel
import com.talangraga.data.local.session.Session
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    private val session: Session
) : ViewModel() {

    private val _profile = MutableStateFlow(session.getProfile()?.toUiData())
    val profile = _profile.asStateFlow()

}
