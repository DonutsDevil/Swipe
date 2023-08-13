package com.swapnil.myapplication.di.module

import com.swapnil.myapplication.constants.NetworkConstants
import com.swapnil.myapplication.network.RetryManager
import com.swapnil.myapplication.network.product.ProductAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {


    @Singleton
    @Provides
    fun provideRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.PRODUCT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(RetryManager(NetworkConstants.HTTP_RETRY_MAX_ATTEMPTS))
        client.connectTimeout(2, TimeUnit.MINUTES)
        client.readTimeout(2, TimeUnit.MINUTES)
        return client.build()
    }

    @Singleton
    @Provides
    fun provideProductsAPI(retrofit: Retrofit): ProductAPI {
        return retrofit.create(ProductAPI::class.java)
    }
}