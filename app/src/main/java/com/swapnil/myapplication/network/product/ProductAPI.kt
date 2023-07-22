package com.swapnil.myapplication.network.product

import com.swapnil.myapplication.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductAPI {
    @GET("/api/public/get")
    suspend fun getAllProducts(): Response<List<Product>?>?
}