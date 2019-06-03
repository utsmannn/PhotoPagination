package com.utsman.wallaz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChipHistoryRoom::class], version = 1)
abstract class ChipHistoryDatabase : RoomDatabase() {

    abstract fun chipHistoryDao(): ChipHistoryDao

    companion object {
        @Volatile private var instance: ChipHistoryDatabase? = null

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context, ChipHistoryDatabase::class.java, "chip-history-db").build()
        fun getInstance(context: Context): ChipHistoryDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
    }
}