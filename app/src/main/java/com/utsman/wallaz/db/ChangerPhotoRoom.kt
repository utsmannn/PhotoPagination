package com.utsman.wallaz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "changer_photo")
data class ChangerPhotoRoom(@PrimaryKey @ColumnInfo(name = "id") val id: String,
                            @ColumnInfo(name = "url") val url: String)