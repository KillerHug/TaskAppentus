package com.mayank.taskappentus.api

import com.mayank.taskappentus.ImageModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("list")
    suspend fun getImage(@Query("page") login: Int): List<ImageModel>
    @GET("list")
    suspend fun getImage(): List<ImageModel>
}