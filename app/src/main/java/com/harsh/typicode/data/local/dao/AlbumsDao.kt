package com.harsh.typicode.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harsh.typicode.data.local.entity.Albums
import io.reactivex.Flowable

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
@Dao
interface AlbumsDao {
    @Query("SELECT * FROM ${Albums.TABLE_NAME} ORDER BY ${Albums.COLUMN_TITLE}")
    fun listenForAlbums(): Flowable<List<Albums>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbums(cats: List<Albums>)

    @Query("SELECT COUNT(*) FROM ${Albums.TABLE_NAME}")
    fun count(): Int
}