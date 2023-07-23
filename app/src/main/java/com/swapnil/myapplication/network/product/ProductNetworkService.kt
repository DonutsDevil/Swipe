package com.swapnil.myapplication.network.product

import android.content.Context
import android.net.Uri
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.repository.State


interface ProductNetworkService {
    suspend fun getAllProducts(): List<Product>
    suspend fun loadAllImages(urlsList: List<String?>, context: Context)
    suspend fun addProductWithFile(
        product: Product,
        imageUri: Uri
    ): State<ProductResponse>

    suspend fun addProductWithoutFile(product: Product): State<ProductResponse>
}