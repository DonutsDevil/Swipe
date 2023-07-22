package com.swapnil.myapplication.network

import android.text.TextUtils
import android.util.Log
import com.swapnil.myapplication.constants.NetworkConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SwipeNetwork {
    companion object {
        private const val TAG = "SwipeNetwork"
        private val INSTANCES = mutableMapOf<String, Retrofit>()

        fun getRetrofitInstance(baseUrl: String): Retrofit? {
            if (TextUtils.isEmpty(baseUrl)) {
                Log.d(TAG, "getRetrofitInstance: baseUrl is empty")
                return null
            }
            return synchronized(INSTANCES) {
                if (INSTANCES.containsKey(baseUrl)) {
                    return@synchronized INSTANCES[baseUrl]
                } else {
                    val client = OkHttpClient.Builder()
                    client.addInterceptor(RetryManager(NetworkConstants.HTTP_RETRY_MAX_ATTEMPTS))
                    client.connectTimeout(2, TimeUnit.MINUTES)
                    client.readTimeout(2, TimeUnit.MINUTES)
                    val instance = getInstance(baseUrl, client)
                    INSTANCES[baseUrl] = instance
                    return@synchronized instance
                }
            }
        }

        private fun getInstance(baseUrl: String, client: OkHttpClient.Builder): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        }
    }
}