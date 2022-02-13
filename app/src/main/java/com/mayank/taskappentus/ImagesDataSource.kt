package com.mayank.taskappentus

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.mayank.taskappentus.api.ApiService
import com.mayank.taskappentus.roomDB.ImageDatabase

class ImagesDataSource(private val api: ApiService, private val db: ImageDatabase) :
    PagingSource<Int, ImageModel>() {
    val dao = db.imageDatabaseDao
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        return try {
            val nextPageNumber = params.key ?: 1

// to get data from api according to page number
            Log.e("DataSource", "load: $nextPageNumber")
            val response: List<ImageModel> = api.getImage(nextPageNumber)
            Log.e("TAG", "size: "+response.size)
//
            db.withTransaction {
//               store all data in DB according to page number
                dao.insertAll(response)
            }
//            manage page no.
            LoadResult.Page(
                data = response,
                prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null,
                nextKey = nextPageNumber + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}