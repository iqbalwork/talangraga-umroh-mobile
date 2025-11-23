package com.talangraga.data.local.session

import com.russhwolf.settings.Settings
import com.talangraga.data.network.model.response.UserResponse
import kotlinx.serialization.json.Json

class Session(
    private val settings: Settings,
    private val json: Json
) {

    fun saveBoolean(key: String, value: Boolean) = settings.putBoolean(key, value)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        settings.getBoolean(key, defaultValue)

    fun saveString(key: String, value: String) = settings.putString(key, value)

    fun getString(key: String, defaultValue: String = ""): String =
        settings.getString(key, defaultValue)

    fun saveInt(key: String, value: Int) = settings.putInt(key, value)

    fun getInt(key: String, defaultValue: Int = 0): Int = settings.getInt(key, defaultValue)

    fun saveLong(key: String, value: Long) = settings.putLong(key, value)

    fun getLong(key: String, defaultValue: Long = 0): Long = settings.getLong(key, defaultValue)

    fun saveFloat(key: String, value: Float) = settings.putFloat(key, value)

    fun getFloat(key: String, defaultValue: Float = 0f): Float =
        settings.getFloat(key, defaultValue)

    fun saveDouble(key: String, value: Double) = settings.putDouble(key, value)

    fun getDouble(key: String, defaultValue: Double = 0.0): Double =
        settings.getDouble(key, defaultValue)

    fun saveProfile(user: com.talangraga.data.network.model.response.UserResponse) {
        val profileString = json.encodeToString(_root_ide_package_.com.talangraga.data.network.model.response.UserResponse.serializer(), user)
        settings.putString(_root_ide_package_.com.talangraga.data.local.session.SessionKey.PROFILE_KEY, profileString)
    }

    fun getProfile(): com.talangraga.data.network.model.response.UserResponse? {
        val profileString = settings.getString(_root_ide_package_.com.talangraga.data.local.session.SessionKey.PROFILE_KEY, "")
        val user = try {
            json.decodeFromString(_root_ide_package_.com.talangraga.data.network.model.response.UserResponse.serializer(), profileString)
        } catch (e: Exception) {
            null
        }
        return user
    }

    fun remove(key: String) = settings.remove(key)

    fun clear() = settings.clear()

}
