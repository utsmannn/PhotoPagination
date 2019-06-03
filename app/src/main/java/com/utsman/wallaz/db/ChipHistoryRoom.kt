package com.utsman.wallaz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_search")
data class ChipHistoryRoom(
    @ColumnInfo(name = "date") val date: Long,
    @PrimaryKey @ColumnInfo(name = "query") val query: String
)