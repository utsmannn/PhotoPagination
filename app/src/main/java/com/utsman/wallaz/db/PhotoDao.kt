package com.utsman.wallaz.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utsman.wallaz.data.Photos

@Dao
interface PhotoDao {
    @Query ("SELECT * FROM photos")
    fun getAllPhoto(): List<Photos>

    @Insert
    fun insert(photos: Photos)

    @Delete
    fun remove(photos: Photos)
}