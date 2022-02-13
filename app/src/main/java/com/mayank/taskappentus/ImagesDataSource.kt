package com.mayank.taskappentus

import android.util.Log
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.mayank.taskappentus.api.ApiService
import com.mayank.taskappentus.roomDB.ImageDatabase

class ImagesDataSource(private val api: ApiService, private val db: ImageDatabase) : PagingSource<Int, ImageModel>() {
    val dao = db.imageDatabaseDao
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        return try {
            val nextPageNumber = params.key ?: 1
            Log.e("DataSource", "pg no.: $nextPageNumber")
            val response: List<ImageModel> = api.getImage(nextPageNumber)
            db.withTransaction {
                Log.e("DataSource", "size: ${response.size}")
                dao.insertAll(response)
//                dao.insert(response[0])
            }
            LoadResult.Page(
                data = response,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}