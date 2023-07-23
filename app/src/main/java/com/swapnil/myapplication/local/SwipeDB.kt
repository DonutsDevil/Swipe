package com.swapnil.myapplication.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.swapnil.myapplication.local.product.ProductDao
import com.swapnil.myapplication.model.Product

@Database(entities = [Product::class], version = 1)
abstract class SwipeDB : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var database: SwipeDB? = null
        private val lock = Any()

        fun getInstance(context: Context): SwipeDB {
            if (database != null) {
                return database!!
            }
            return synchronized(lock) {
                if (database != null) {
                    database!!
                } else {
                    Room.databaseBuilder(
                        context.applicationContext,
                        SwipeDB::class.java,
                        "swipe_database"
                    ).build()
                }
            }
        }
    }
}