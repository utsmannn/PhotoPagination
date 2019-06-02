package com.utsman.wallaz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_bookmark")
data class PhotoRoom(@PrimaryKey @ColumnInfo(name = "id") val id: String,
                     @ColumnInfo(name = "url") val url: String,
                     @ColumnInfo(name = "millis") val millis: Long)