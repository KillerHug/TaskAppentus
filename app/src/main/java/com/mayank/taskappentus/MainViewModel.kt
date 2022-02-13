package com.mayank.taskappentus

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.mayank.taskappentus.api.ApiService
import com.mayank.taskappentus.api.Resource
import com.mayank.taskappentus.api.RetrofitBuilder
import com.mayank.taskappentus.roomDB.ImageDatabase
import com.mayank.taskappentus.roomDB.ImageDatabaseDao
import kotlinx.coroutines.Dispatchers


class MainViewModel(dao: ImageDatabaseDao, context: Context) : ViewModel() {
    val allImages = dao.getAllImage()

    private val apiService = RetrofitBuilder.getInstance().create(ApiService::class.java)

    fun getImage() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiService.getImage(1)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    val images =
        Pager(config = PagingConfig(pageSize = PAGE_SIZE), pagingSourceFactory = {
            ImagesDataSource(apiService, ImageDatabase.getInstance(context))
        }).flow.cachedIn(viewModelScope)

    companion object {
        private const val PAGE_SIZE = 30
    }
}