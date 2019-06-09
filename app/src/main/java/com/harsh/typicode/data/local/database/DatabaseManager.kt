package com.harsh.typicode.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harsh.typicode.data.local.dao.AlbumsDao
import com.harsh.typicode.data.local.entity.Albums

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
@Database(entities = [Albums::class], version = 1)
abstract class DatabaseManager : RoomDatabase() {
    abstract val albumsDao: AlbumsDao
}