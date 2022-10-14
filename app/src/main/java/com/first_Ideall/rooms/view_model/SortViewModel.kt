package com.first_Ideall.rooms.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.first_Ideall.enums.SortCaller
import com.first_Ideall.rooms.data.SortData
import com.first_Ideall.rooms.repository.SortRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SortViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SortRepository.getInstance(application)
    private val sortData = repository.getData()

    fun getData(): LiveData<SortData> {
        return sortData
    }

    fun getDataByCaller(caller: SortCaller): LiveData<SortData> {
        return repository.getDataByCaller(caller)
    }

    fun insert(data: SortData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(data)
    }

    fun delete(data: SortData) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(data)
    }
}