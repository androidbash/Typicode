package com.harsh.typicode.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harsh.typicode.data.local.entity.Albums.Companion.TABLE_NAME

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
@Entity(tableName = TABLE_NAME)
data class Albums(
        @PrimaryKey
        val id: Int,
        val title: String,
        val userId: Int
) {
    companion object {
        const val TABLE_NAME = "albums"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_USERID = "userId"
    }
}