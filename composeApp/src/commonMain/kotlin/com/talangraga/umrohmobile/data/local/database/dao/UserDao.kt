package com.talangraga.umrohmobile.data.local.database.dao // Or your preferred dao package

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE user_id = :userId LIMIT 1")
    fun getUserById(userId: Int): Flow<UserEntity?>

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} LIMIT 1")
    fun getActiveUser(): Flow<UserEntity?>

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME}")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("DELETE FROM ${UserEntity.TABLE_NAME} WHERE user_id = :userId")
    suspend fun deleteUserById(userId: Int): Int

    @Query("DELETE FROM ${UserEntity.TABLE_NAME}")
    suspend fun deleteAllUsers()
}
