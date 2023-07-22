package com.swapnil.myapplication.repository

import android.content.Context
import android.util.Log
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.network.product.ProductNetworkService
import java.io.IOException

private const val TAG = "ProductRepository"
class ProductRepository(private val networkService: ProductNetworkService) {

    suspend fun getAllProducts(context: Context): State<List<Product>> {
        return try {
            val productList = networkService.getAllProducts()
            val imageUrlList = productList.map { it.images }
            networkService.loadAllImages(imageUrlList, context)
            State.Success(data = productList)
        } catch (e: IOException) {
            Log.e(TAG, "getAllProducts: error occurred", e)
            State.Error(errorMessage = e.toString())
        }
    }
}