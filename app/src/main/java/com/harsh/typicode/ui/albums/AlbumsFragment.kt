package com.harsh.typicode.ui.albums

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.typicode.R
import com.harsh.typicode.TypiApplication
import com.harsh.typicode.utils.RecyclerViewScrollBottomOnSubscribe
import com.harsh.typicode.utils.createViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_albums.*


/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class AlbumsFragment : Fragment() {
    private lateinit var albumsViewModel: AlbumsViewModel

    private lateinit var albumsAdapter: AlbumsAdapter

    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val application = context.applicationContext as TypiApplication
        albumsViewModel = createViewModel { AlbumsViewModel(application.albumsRepository) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_albums, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                (layoutManager as LinearLayoutManager).orientation
            )

            this.addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
            adapter = AlbumsAdapter(albumsViewModel).also {
                albumsAdapter = it
            }
        }

        compositeDisposable += albumsViewModel.observeAlbums(onNext = { cats ->
            albumsAdapter.updateData(cats)
        })

        compositeDisposable += Observable.create(RecyclerViewScrollBottomOnSubscribe(recyclerView))
            .subscribeBy(onNext = { isScroll ->
                albumsViewModel.handleScrollToBottom(isScroll)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}