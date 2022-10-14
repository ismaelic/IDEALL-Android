package com.first_Ideall.rooms.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.first_Ideall.rooms.dao.ColorDao
import com.first_Ideall.rooms.data.ColorData
import com.first_Ideall.rooms.database.AppDatabase

class ColorRepository(application: Application) {
    companion object {
        private var colorRepoInstance: ColorRepository? = null

        fun getInstance(app: Application): ColorRepository {
            return colorRepoInstance ?: synchronized(this) {
                colorRepoInstance ?: ColorRepository(app).also { colorRepoInstance = it }
            }
        }
    }

    private val db: AppDatabase by lazy {
        AppDatabase.getInstance(application.applicationContext)
    }
    private val colorDao: ColorDao by lazy {
        db.colorDao()
    }

    private val colorData: LiveData<List<ColorData>> by lazy {
        colorDao.getColorData()
    }

    fun getData(): LiveData<List<ColorData>> {
        return colorData
    }

    suspend fun insert(data: ColorData) {
        colorDao.insertData(data)
    }

    suspend fun delete(data: ColorData) {
        colorDao.insertData(data)
    }

    suspend fun insertNewDeleteOld(newData: ColorData, oldData: ColorData) {
        db.withTransaction {
            colorDao.insertData(newData)
            colorDao.deleteData(oldData)
        }
    }
}