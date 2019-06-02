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