package com.talangraga.umrohmobile.data.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.talangraga.umrohmobile.data.local.database.model.UserEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "fullname")
    val fullname: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "domisili")
    val domisili: String,
    @ColumnInfo(name = "user_type")
    val userType: String,
    @ColumnInfo(name = "image_profile_url")
    val imageProfileUrl: String,
) {
    companion object {
        const val TABLE_NAME = "user_data"
    }
}
