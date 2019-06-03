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