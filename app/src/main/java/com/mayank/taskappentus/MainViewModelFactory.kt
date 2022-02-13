package com.mayank.taskappentus

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayank.taskappentus.roomDB.ImageDatabaseDao
import java.lang.IllegalArgumentException

class MainViewModelFactory(
    private val dao: ImageDatabaseDao,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao,context) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}