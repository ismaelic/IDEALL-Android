package com.first_Ideall.rooms.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.first_Ideall.enums.SortCaller
import com.first_Ideall.rooms.dao.SortDao
import com.first_Ideall.rooms.data.SortData
import com.first_Ideall.rooms.database.AppDatabase

class SortRepository(application: Application) {
    companion object {
        private var sortRepoInstance: SortRepository? = null

        fun getInstance(app: Application): SortRepository {
            return sortRepoInstance ?: synchronized(this) {
                sortRepoInstance ?: SortRepository(app).also { sortRepoInstance = it }
            }
        }
    }

    private val sortDao: SortDao by lazy {
        val db = AppDatabase.getInstance(application.applicationContext)
        db.sortDao()
    }

    private val sortData: LiveData<SortData> by lazy {
        sortDao.getSortData()
    }

    fun getData(): LiveData<SortData> {
        return sortData
    }

    fun getDataByCaller(caller: SortCaller): LiveData<SortData> {
        return sortDao.getSortDataByCaller(caller)
    }


    suspend fun insert(data: SortData) {
        sortDao.insertSortData(data)
    }

    suspend fun delete(data: SortData) {
        sortDao.deleteSortData(data)
    }
}