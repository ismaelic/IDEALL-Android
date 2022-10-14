package com.first_Ideall.rooms.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.first_Ideall.enums.OrderEnum
import com.first_Ideall.enums.SortCaller
import com.first_Ideall.enums.SortEnum
import java.io.Serializable

@Entity(tableName = "sort_data")
data class SortData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sort_id") var sortId: Long?,
    @ColumnInfo(name = "caller") var caller: SortCaller,
    @ColumnInfo(name = "sort_enum") var sortEnum: SortEnum = SortEnum.TITLE,
    @ColumnInfo(name = "order_enum") var orderEnum: OrderEnum = OrderEnum.ASC
) : Serializable