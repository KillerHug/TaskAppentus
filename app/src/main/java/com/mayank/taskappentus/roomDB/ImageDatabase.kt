package com.mayank.taskappentus.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mayank.taskappentus.ImageModel
import java.lang.Exception

@Database(entities = [ImageModel::class], version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {
    abstract val imageDatabaseDao: ImageDatabaseDao
    companion object {
        @Volatile
        private var INSTANCE: ImageDatabase? = null
//create database instance
        fun getInstance(context: Context): ImageDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, ImageDatabase::class.java,
                        "image_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}