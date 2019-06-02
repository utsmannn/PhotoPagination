package com.utsman.wallaz.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utsman.wallaz.data.Photos
import io.reactivex.Observable

@Dao
interface PhotoDao {
    @Query ("SELECT * FROM photo_bookmark")
    fun getAllPhoto(): Observable<MutableList<PhotoRoom>>

    @Query("SELECT * FROM photo_bookmark ORDER BY millis DESC")
    fun getPagedPhoto(): DataSource.Factory<Int, PhotoRoom>

    @Query("SELECT * FROM photo_bookmark WHERE id = :id")
    fun getPhoto(id: String): LiveData<PhotoRoom>

    @Insert
    fun insert(photoRoom: PhotoRoom)

    @Delete
    fun remove(photoRoom: PhotoRoom)
}