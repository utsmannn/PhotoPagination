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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhotoRoom::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile private var instance: PhotoDatabase? = null

        private fun buildData(context: Context) = Room.databaseBuilder(context, PhotoDatabase::class.java, "bookmarked-db").build()

        fun getInstance(context: Context): PhotoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildData(context).also { instance = it }
            }
        }
    }
}