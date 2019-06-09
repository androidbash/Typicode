package com.harsh.typicode.utils

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.MainThreadDisposable


/**
 * Created by Harsh Mittal on 2019-06-07.
 */

class RecyclerViewScrollBottomOnSubscribe(val view: androidx.recyclerview.widget.RecyclerView) : ObservableOnSubscribe<Boolean> {
    override fun subscribe(emitter: ObservableEmitter<Boolean>) {
        MainThreadDisposable.verifyMainThread()

        val watcher = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom()
                }
            }

            fun onScrolledToBottom() {
                if (!emitter.isDisposed) {
                    emitter.onNext(true)
                }
            }
        }

        emitter.setDisposable(object : MainThreadDisposable() {
            override fun onDispose() {
                view.removeOnScrollListener(watcher)
            }
        })

        view.addOnScrollListener(watcher)

        // Emit initial value.
        emitter.onNext(false)
    }
}
