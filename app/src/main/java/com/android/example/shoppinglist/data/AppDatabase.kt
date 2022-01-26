package com.android.example.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {

        private val LOCK = Any()
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "shop_item.db"

        // pattern single tone
        fun getInstance(application: Application): AppDatabase {
            // doubleCheck
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                // doubleCheck
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = db
                return db
            }
        }
    }

}