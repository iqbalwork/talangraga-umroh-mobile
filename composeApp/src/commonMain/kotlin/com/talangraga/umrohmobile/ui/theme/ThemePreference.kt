package com.talangraga.umrohmobile.ui.theme

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { LIGHT, DARK, SYSTEM }

@OptIn(ExperimentalSettingsApi::class)
class ThemePreference(private val settings: ObservableSettings) {

    companion object {
        private const val THEME_KEY = "theme_mode"
        private const val DYNAMIC_COLOR_KEY = "dynamic_color_enabled"
    }

    fun getTheme(): ThemeMode =
        ThemeMode.valueOf(settings.getString(THEME_KEY, ThemeMode.SYSTEM.name))

    fun setTheme(mode: ThemeMode) =
        settings.putString(THEME_KEY, mode.name)

    fun getThemeFlow(): Flow<ThemeMode> =
        settings.toFlowSettings()
            .getStringFlow(THEME_KEY, ThemeMode.SYSTEM.name)
            .map { ThemeMode.valueOf(it) }

    fun getDynamicColor(): Boolean =
        settings.getBoolean(DYNAMIC_COLOR_KEY, true)

    fun setDynamicColor(enabled: Boolean) =
        settings.putBoolean(DYNAMIC_COLOR_KEY, enabled)

    fun getDynamicColorFlow(): Flow<Boolean> =
        settings.toFlowSettings()
            .getBooleanFlow(DYNAMIC_COLOR_KEY, true)
}

