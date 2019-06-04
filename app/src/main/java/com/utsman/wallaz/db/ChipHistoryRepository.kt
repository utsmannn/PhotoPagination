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

class ChipHistoryRepository private constructor(private val chipHistoryDao: ChipHistoryDao) {

    fun getAllChips() = chipHistoryDao.getAllChips()
    fun getChip(query: String) = chipHistoryDao.getChip(query)
    fun insertChip(chipHistoryRoom: ChipHistoryRoom) = chipHistoryDao.insert(chipHistoryRoom)
    fun deleteChip(chipHistoryRoom: ChipHistoryRoom) = chipHistoryDao.delete(chipHistoryRoom)

    companion object {
        @Volatile private var instance: ChipHistoryRepository? = null
        fun getInstance(chipHistoryDao: ChipHistoryDao) = instance ?: synchronized(this) {
            instance ?: ChipHistoryRepository(chipHistoryDao).also { instance = it }
        }
    }
}