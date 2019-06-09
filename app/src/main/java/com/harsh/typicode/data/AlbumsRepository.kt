package com.harsh.typicode.data

import android.util.Log
import com.harsh.typicode.data.local.dao.AlbumsDao
import com.harsh.typicode.data.local.entity.Albums
import com.harsh.typicode.data.remote.task.AlbumsTaskFactory
import com.harsh.typicode.data.remote.task.AlbumsTaskManager
import com.harsh.typicode.utils.Scheduler
import io.reactivex.Flowable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Harsh Mittal on 2019-06-07.
 */

class AlbumsRepository(
    val albumsDao: AlbumsDao,
    val albumsTaskManager: AlbumsTaskManager,
    val albumsTaskFactory: AlbumsTaskFactory,
    val mainScheduler: Scheduler
) {
    companion object {
        const val TAG = "CatRepository"
    }

    fun startListeningForCats(): Flowable<List<Albums>> =
        albumsDao.listenForAlbums()
            .distinctUntilChanged()
            .observeOn(mainScheduler.asRxScheduler())
            .replay(1)
            .autoConnect(0)

    private fun isNoDataDownloaded(): Boolean = albumsDao.count() == 0

    private fun isDownloadInProgress(): Boolean =
        albumsTaskManager.isRegistered(AlbumsTaskManager.SAVE_ALBUMS_TASK)

    fun startFetch(scrolled: Boolean) {
        if (!isDownloadInProgress() && (scrolled || isNoDataDownloaded())) {
            Log.i(TAG, "Running fetch task.")
            albumsTaskFactory.createSaveAlbumsTask().subscribeBy(onSuccess = {
                // Success
            }, onError = { throwable ->
                Log.i(TAG, "Fetching failed", throwable)
            })
        }
    }
}