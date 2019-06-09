package com.harsh.typicode.data.remote.service

import com.harsh.typicode.data.local.entity.Albums
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Harsh Mittal on 2019-06-07.
 */

interface AlbumsService {
    @GET("albums")
    fun getAlbums(): Single<List<Albums>>
}