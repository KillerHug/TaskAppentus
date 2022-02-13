package com.mayank.taskappentus

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.mayank.taskappentus.api.ApiService
import com.mayank.taskappentus.api.RetrofitBuilder
import com.mayank.taskappentus.roomDB.ImageDatabase
import com.mayank.taskappentus.roomDB.ImageDatabaseDao


class MainViewModel(dao: ImageDatabaseDao, context: Context) : ViewModel() {
    val allImages = dao.getAllImage()

    //instance for api service
    private val apiService = RetrofitBuilder.getInstance().create(ApiService::class.java)

//    paging 3 for get data and pagination
    val images =
        Pager(config = PagingConfig(pageSize = PAGE_SIZE), pagingSourceFactory = {
            ImagesDataSource(apiService, ImageDatabase.getInstance(context))
        }).flow.cachedIn(viewModelScope)

    companion object {
//        count of item in single page
        private const val PAGE_SIZE = 30
    }
}