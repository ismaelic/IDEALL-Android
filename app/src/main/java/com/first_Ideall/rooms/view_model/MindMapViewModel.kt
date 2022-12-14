package com.first_Ideall.rooms.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.first_Ideall.rooms.data.MindMapItemData
import com.first_Ideall.rooms.repository.MindMapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MindMapViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MindMapRepository(application)
    private var mindMapItems: LiveData<List<MindMapItemData>>? = null

    fun getAll(): LiveData<List<MindMapItemData>> {
        return repository.getAll()
    }

    fun getAllByGroupId(groupId: Long): LiveData<List<MindMapItemData>> {
        return mindMapItems ?: synchronized(this) {
            mindMapItems ?: repository.getAllByGroupId(groupId).also { mindMapItems = it }
        }
    }

    fun insert(item: MindMapItemData, func: (id: Long) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            func(repository.insert(item))
        }

    fun update(item: MindMapItemData) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(item)
    }

    fun delete(item: MindMapItemData) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(item)
    }

    fun deleteByGroupId(itemGroupId: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByGroupId(itemGroupId)
    }

    fun deleteItems(itemGroupIds: List<Long>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteItems(itemGroupIds)
    }
}