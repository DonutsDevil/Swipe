package com.swapnil.myapplication.di.module

import com.swapnil.myapplication.network.product.ProductAPI
import com.swapnil.myapplication.network.product.ProductNetworkService
import com.swapnil.myapplication.network.product.ProductNetworkServiceImpl
import dagger.Module
import dagger.Provides

@Module
class NetworkServiceModule {

    @Provides
    fun provideProductNetworkServiceImpl(productAPI: ProductAPI): ProductNetworkService {
        return ProductNetworkServiceImpl(productAPI)
    }
}