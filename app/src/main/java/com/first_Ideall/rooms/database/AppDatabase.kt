package com.first_Ideall.rooms.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.first_Ideall.R
import com.first_Ideall.rooms.converter.IdeaTypeConverter
import com.first_Ideall.enums.SortCaller
import com.first_Ideall.rooms.dao.*
import com.first_Ideall.rooms.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MindMapItemData::class, IdeaData::class, SeriesData::class, SortData::class, ColorData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(IdeaTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mindMapItemDao(): MindMapItemDao
    abstract fun ideaDao(): IdeaDao
    abstract fun seriesDao(): SeriesDao
    abstract fun sortDao(): SortDao
    abstract fun colorDao(): ColorDao

    companion object {
        private const val DB_NAME = "mind_blooming_db"
        private var dbInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return dbInstance ?: synchronized(AppDatabase::class) {
                dbInstance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val date = System.currentTimeMillis()
                            val data = SeriesData(
                                null,
                                context.getString(R.string.drawer_series_one),
                                context.getString(R.string.drawer_series_one),
                                date,
                                date,
                                true
                            )
                            getInstance(context).seriesDao().insertSeries(data)

                            data.seriesTitle = context.getString(R.string.drawer_series_two)
                            data.seriesDescription = context.getString(R.string.drawer_series_two)
                            getInstance(context).seriesDao().insertSeries(data)

                            data.seriesTitle = context.getString(R.string.drawer_series_three)
                            data.seriesDescription = context.getString(R.string.drawer_series_three)
                            getInstance(context).seriesDao().insertSeries(data)

                            val sortData = SortData(null, SortCaller.MAIN)
                            getInstance(context).sortDao().insertSortData(sortData)

                            sortData.caller = SortCaller.STARRED
                            getInstance(context).sortDao().insertSortData(sortData)

                            sortData.caller = SortCaller.SERIES
                            getInstance(context).sortDao().insertSortData(sortData)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                .also { dbInstance = it }
        }
    }
}