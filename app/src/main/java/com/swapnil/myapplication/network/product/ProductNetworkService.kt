package com.swapnil.myapplication.network.product

import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse


interface ProductNetworkService {
    suspend fun getAllProducts() : List<Product>
    suspend fun addProduct(product: Product): ProductResponse?
}