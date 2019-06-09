package com.harsh.typicode.data.remote.task

import android.util.Log
import com.harsh.typicode.data.local.dao.AlbumsDao
import com.harsh.typicode.data.local.entity.Albums
import com.harsh.typicode.data.remote.service.AlbumsService
import com.harsh.typicode.data.remote.task.AlbumsTaskManager.Companion.SAVE_ALBUMS_TASK
import com.harsh.typicode.utils.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class AlbumsTaskFactory(
        val albumsTaskManager: AlbumsTaskManager,
        val albumsService: AlbumsService,
        val albumsDao: AlbumsDao,
        val networkScheduler: Scheduler,
        val ioScheduler: Scheduler
) {
    fun createSaveAlbumsTask(): Single<Unit> = Single.create { emitter ->
        albumsTaskManager.registerTask(SAVE_ALBUMS_TASK)
        albumsService.getAlbums()
                .subscribeOn(networkScheduler.asRxScheduler())
                .observeOn(ioScheduler.asRxScheduler())
                .doFinally { albumsTaskManager.unregisterTask(SAVE_ALBUMS_TASK) }
                .subscribeBy(onSuccess = { albums ->
                    if (albums == null) {
                        emitter.onSuccess(Unit)
                        return@subscribeBy
                    }
                    val maxRank = albumsDao.count()
                    albums.mapIndexed { index: Int, albums: Albums ->
                        with(albums) {
                            Albums(albums.id, albums.title, albums.userId)
                        }
                    }.let { albumsList ->
                        Log.i(SAVE_ALBUMS_TASK, "Saving cats to database.")
                        albumsDao.insertAlbums(albumsList)
                    }
                    emitter.onSuccess(Unit)
                }, onError = { throwable ->
                    emitter.onError(throwable)
                })
    }
}