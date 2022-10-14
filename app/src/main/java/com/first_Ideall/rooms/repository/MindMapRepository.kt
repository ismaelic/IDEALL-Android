package com.first_Ideall.rooms.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.first_Ideall.rooms.dao.MindMapItemDao
import com.first_Ideall.rooms.data.MindMapItemData
import com.first_Ideall.rooms.database.AppDatabase

class MindMapRepository(application: Application) {
    private val mindMapItemDao: MindMapItemDao by lazy {
        val db = AppDatabase.getInstance(application.applicationContext)
        db.mindMapItemDao()
    }

    fun getAll():LiveData<List<MindMapItemData>> {
        return mindMapItemDao.getAllItems()
    }

    fun getAllByGroupId(groupId: Long): LiveData<List<MindMapItemData>> {
        return mindMapItemDao.getItemsByGroupId(groupId)
    }

    suspend fun insert(item: MindMapItemData): Long {
        return mindMapItemDao.insertItem(item)
    }

    suspend fun update(item: MindMapItemData) {
        mindMapItemDao.updateItem(item)
    }

    suspend fun delete(item: MindMapItemData) {
        mindMapItemDao.deleteItem(item)
    }

    suspend fun deleteByGroupId(itemGroupId: Long) {
        mindMapItemDao.deleteItemByGroupId(itemGroupId)
    }

    suspend fun deleteItems(itemGroupIds: List<Long>) {
        mindMapItemDao.deleteItemInList(itemGroupIds)
    }
}