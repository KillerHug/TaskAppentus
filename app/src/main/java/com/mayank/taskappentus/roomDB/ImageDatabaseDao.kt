package com.mayank.taskappentus.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mayank.taskappentus.ImageModel

@Dao
interface ImageDatabaseDao {
    @Insert
    fun insertAll(imageList: List<ImageModel>)

    @Insert
    fun insert(imageList: ImageModel)

    @Query("SELECT * FROM IMAGE_TABLE ORDER BY ids ASC")
    fun getAllImage(): LiveData<List<ImageModel>>
}