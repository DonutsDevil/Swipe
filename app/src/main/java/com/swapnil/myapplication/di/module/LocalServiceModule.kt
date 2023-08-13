package com.swapnil.myapplication.di.module

import android.content.Context
import androidx.room.Room
import com.swapnil.myapplication.local.SwipeDB
import com.swapnil.myapplication.local.product.ProductDao
import com.swapnil.myapplication.local.product.ProductLocalService
import com.swapnil.myapplication.local.product.ProductLocalServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocalServiceModule {

    @Provides
    fun provideProductLocalServiceImpl(productDao: ProductDao): ProductLocalService {
        return ProductLocalServiceImpl(productDao)
    }

    @Singleton
    @Provides
    fun provideSwipeDb(context: Context): SwipeDB {
        return Room.databaseBuilder(
            context.applicationContext,
            SwipeDB::class.java,
            "swipe_database"
        ).build()
    }

    @Provides
    fun provideProductDao(swipeDB: SwipeDB): ProductDao {
        return swipeDB.productDao()
    }
}