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

class PhotosRepository private constructor(private val photoDao: PhotoDao) {

    fun getPagedPhoto() = photoDao.getPagedPhoto()
    fun insertPhoto(photoRoom: PhotoRoom) = photoDao.insert(photoRoom)
    fun deletePhoto(photoRoom: PhotoRoom) = photoDao.remove(photoRoom)
    fun checkPhoto(id: String) = photoDao.getPhoto(id)

    companion object {
        @Volatile private var instance: PhotosRepository? = null
        fun getInstance(photoDao: PhotoDao) = instance ?: synchronized(this) {
            instance ?: PhotosRepository(photoDao).also { instance = it }
        }
    }
}