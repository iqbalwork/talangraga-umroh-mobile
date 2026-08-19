package com.talangraga.umrohmobile.ui.theme

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeManager(private val preference: ThemePreference) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val _themeMode = MutableStateFlow(preference.getTheme())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _isDynamicColor = MutableStateFlow(preference.getDynamicColor())
    val isDynamicColor: StateFlow<Boolean> = _isDynamicColor.asStateFlow()

    init {
        coroutineScope.launch {
            preference.getThemeFlow().collect { _themeMode.value = it }
        }
        coroutineScope.launch {
            preference.getDynamicColorFlow().collect { _isDynamicColor.value = it }
        }
    }

    fun setTheme(mode: ThemeMode) {
        coroutineScope.launch { preference.setTheme(mode) }
    }

    fun setDynamicColor(enabled: Boolean) {
        coroutineScope.launch { preference.setDynamicColor(enabled) }
    }
}

