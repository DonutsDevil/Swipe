package com.swapnil.myapplication.repository

import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.network.product.ProductNetworkService

class ProductRepository(private val networkService: ProductNetworkService) {

    suspend fun getAllProducts(): List<Product> {
        return networkService.getAllProducts()
    }
}