package com.swapnil.myapplication.network.product

import android.content.Context
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse


interface ProductNetworkService {
    suspend fun getAllProducts() : List<Product>
    suspend fun loadAllImages(urlsList: List<String?>, context: Context)
    suspend fun addProduct(product: Product): ProductResponse?
}