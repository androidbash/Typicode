package com.harsh.typicode.ui.albums

import androidx.lifecycle.ViewModel
import com.harsh.typicode.data.AlbumsRepository
import com.harsh.typicode.data.local.entity.Albums
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class AlbumsViewModel(
        val albumsRepository: AlbumsRepository
) : ViewModel() {
    val albums: Flowable<List<Albums>> = albumsRepository.startListeningForCats()

    fun observeAlbums(onNext: (List<Albums>) -> Unit, onError: (Throwable) -> Unit = {}): Disposable =
        albums.subscribeBy(onNext = onNext, onError = onError)

    fun handleScrollToBottom(scrolled: Boolean) {
        albumsRepository.startFetch(scrolled)
    }


}