package com.harsh.typicode.ui.albums

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.harsh.typicode.R
import com.harsh.typicode.data.local.entity.Albums
import com.harsh.typicode.utils.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_album.*
import java.util.*

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class AlbumsAdapter(val albumsViewModel: AlbumsViewModel) : RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_album))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    private var list: List<Albums> = Collections.emptyList()

    fun updateData(list: List<Albums>?) {
        this.list = list ?: Collections.emptyList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(albums: Albums) {
            albumTitle.text = albums.title
            itemContainer.setOnClickListener { view ->
                //do something
            }
        }
    }
}