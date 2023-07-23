package com.swapnil.myapplication.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.swapnil.myapplication.local.product.ProductLocalService
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.network.product.ProductNetworkService
import java.io.IOException

private const val TAG = "ProductRepository"
class ProductRepository(private val networkService: ProductNetworkService, private val localService: ProductLocalService) {

    suspend fun getAllProducts(context: Context): State<List<Product>> {
        return try {
            val productList = networkService.getAllProducts()
            localService.deleteAllProducts(context)
            localService.addProducts(productList, context)
            val imageUrlList = productList.map { it.images }
            networkService.loadAllImages(imageUrlList, context)
            State.Success(data = productList)
        } catch (e: IOException) {
            Log.e(TAG, "getAllProducts: error occurred", e)
            val productList = localService.getAll(context)
            if (productList.isEmpty()) {
                State.Error(errorMessage = e.toString())
            } else {
                State.Success(productList)
            }
        }
    }

    suspend fun addProduct(
        product: Product,
        imageUri: Uri? = null
    ): State<ProductResponse> {
        return try {
            if (imageUri != null) {
                networkService.addProductWithFile(product, imageUri)
            } else {
                networkService.addProductWithoutFile(product)
            }
        } catch (e: IOException) {
            Log.e(TAG, "addProduct: error occurred", e)
            State.Error(errorMessage = e.toString())
        } catch (e: IOException) {
            Log.e(TAG, "addProduct: error occurred", e)
            State.Error(errorMessage = e.toString())
        }
    }
}