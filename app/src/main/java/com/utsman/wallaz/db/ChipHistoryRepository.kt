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