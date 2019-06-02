package com.utsman.wallaz.db

import com.utsman.wallaz.data.Photos

class PhotosRepository private constructor(private val photoDao: PhotoDao) {

    fun getPhoto() = photoDao.getAllPhoto()
    fun insertPhoto(photos: Photos) = photoDao.insert(photos)
    fun deletePhoto(photos: Photos) = photoDao.remove(photos)

    companion object {
        @Volatile private var instance: PhotosRepository? = null
        fun getInstance(photoDao: PhotoDao) = instance ?: synchronized(this) {
            instance ?: PhotosRepository(photoDao).also { instance = it }
        }
    }
}