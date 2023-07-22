package com.swapnil.myapplication.repository

import android.content.Context
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.network.product.ProductNetworkService

class ProductRepository(private val networkService: ProductNetworkService) {

    suspend fun getAllProducts(context: Context): List<Product> {
        val productList = networkService.getAllProducts()
        val imageUrlList = productList.map { it.images }
        networkService.loadAllImages(imageUrlList, context)
        return productList
    }
}