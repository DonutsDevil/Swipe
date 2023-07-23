package com.swapnil.myapplication.network.product

import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductAPI {
    @GET("/api/public/get")
    suspend fun getAllProducts(): Response<List<Product>?>?

    @Multipart
    @POST("/api/public/add")
    suspend fun createProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: MultipartBody.Part
    ): Response<ProductResponse?>?

    @Multipart
    @POST("/api/public/add")
    suspend fun createProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
    ): Response<ProductResponse?>?
}