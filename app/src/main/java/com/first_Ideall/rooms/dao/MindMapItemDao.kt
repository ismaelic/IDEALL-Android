package com.first_Ideall.rooms.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.first_Ideall.rooms.data.MindMapItemData


@Dao
interface MindMapItemDao {

    @Query("SELECT * FROM mind_map_item_data")
    fun getAllItems(): LiveData<List<MindMapItemData>>

    @Query("SELECT * FROM mind_map_item_data WHERE item_group_id = (:itemGroupId)")
    fun getItemsByGroupId(itemGroupId: Long): LiveData<List<MindMapItemData>>

    @Insert(onConflict = REPLACE)
    suspend fun insertItem(data: MindMapItemData): Long

    @Update
    suspend fun updateItem(data: MindMapItemData)

    @Delete
    suspend fun deleteItem(data: MindMapItemData)

    @Query("DELETE FROM mind_map_item_data WHERE item_group_id = (:itemGroupId)")
    suspend fun deleteItemByGroupId(itemGroupId: Long)

    @Query("DELETE FROM mind_map_item_data WHERE item_group_id IN (:itemGroupIds)")
    suspend fun deleteItemInList(itemGroupIds: List<Long>)
}