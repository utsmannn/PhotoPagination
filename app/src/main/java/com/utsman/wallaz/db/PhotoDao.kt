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
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utsman.wallaz.data.Photos
import io.reactivex.Observable

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photo_bookmark ORDER BY millis DESC")
    fun getPagedPhoto(): DataSource.Factory<Int, PhotoRoom>

    @Query("SELECT * FROM photo_bookmark WHERE id = :id")
    fun getPhoto(id: String): LiveData<PhotoRoom>

    @Insert
    fun insert(photoRoom: PhotoRoom)

    @Delete
    fun remove(photoRoom: PhotoRoom)
}