package com.swapnil.myapplication.network

import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.swapnil.myapplication.constants.NetworkConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SwipeNetwork {
    companion object {
        private const val TAG = "SwipeNetwork"
        private val INSTANCES = mutableMapOf<String, Retrofit>()

        /**
         * Create a Retrofit instance, if already created will return the created one rather then creating new
         * @param baseUrl: url used as key which retrofit instance is created
         * @param gson: any gson to be added in option
         * @param forceCreation: will create a new retrofit instance and overwrite the [baseUrl] retrofit if present
         *
         * @return retrofit instance
         */
        fun getRetrofitInstance(baseUrl: String, gson: Gson? = null, forceCreation: Boolean = false): Retrofit? {
            if (TextUtils.isEmpty(baseUrl)) {
                Log.d(TAG, "getRetrofitInstance: baseUrl is empty")
                return null
            }
            return synchronized(INSTANCES) {
                if (INSTANCES.containsKey(baseUrl) && !forceCreation) {
                    return@synchronized INSTANCES[baseUrl]
                } else {
                    val client = OkHttpClient.Builder()
                    client.addInterceptor(RetryManager(NetworkConstants.HTTP_RETRY_MAX_ATTEMPTS))
                    client.connectTimeout(2, TimeUnit.MINUTES)
                    client.readTimeout(2, TimeUnit.MINUTES)
                    val instance = getInstance(baseUrl, client, gson)
                    INSTANCES[baseUrl] = instance
                    return@synchronized instance
                }
            }
        }

        private fun getInstance(baseUrl: String, client: OkHttpClient.Builder, gson: Gson?): Retrofit {
            val gsonConverterFactory = if (gson == null) {
                GsonConverterFactory.create()
            } else {
                GsonConverterFactory.create(gson)
            }
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .client(client.build())
                .build()
        }
    }
}