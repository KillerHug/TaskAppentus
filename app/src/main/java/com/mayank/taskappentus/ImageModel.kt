package com.mayank.taskappentus

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "IMAGE_TABLE")
data class ImageModel(
    @PrimaryKey(autoGenerate = true)
    var ids: Int = 0,
    var id: String, var author: String,
    var width: Int, var height: Int,
    var url: String, var download_url: String
)
