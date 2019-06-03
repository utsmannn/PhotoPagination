package com.utsman.wallaz.db

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer

@Dao
interface ChipHistoryDao {

    @Query("SELECT * FROM history_search ORDER BY date DESC")
    fun getAllChips(): List<ChipHistoryRoom>

    @Query("SELECT * FROM history_search WHERE `query` = :query")
    fun getChip(query: String): LiveData<ChipHistoryRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chipHistoryRoom: ChipHistoryRoom)

    @Delete
    fun delete(chipHistoryRoom: ChipHistoryRoom)
}