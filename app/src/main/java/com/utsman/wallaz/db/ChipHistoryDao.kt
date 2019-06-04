/*
 * Copyright 2019 Muhammad Utsman. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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