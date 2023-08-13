package com.swapnil.myapplication.di.module

import com.swapnil.myapplication.local.product.ProductLocalService
import com.swapnil.myapplication.local.product.ProductLocalServiceImpl
import dagger.Module
import dagger.Provides


@Module
class LocalServiceModule {

    @Provides
    fun provideProductLocalServiceImpl(): ProductLocalService {
        return ProductLocalServiceImpl()
    }
}