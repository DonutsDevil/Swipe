package com.swapnil.myapplication.network.product


import android.util.Log
import com.swapnil.myapplication.constants.NetworkConstants
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.network.SwipeNetwork


class ProductNetworkServiceImpl: ProductNetworkService {
    private val TAG = "ProductNetworkServiceIm"

    override suspend fun getAllProducts(): List<Product> {
        val response = getRetrofitInstance().getAllProducts()
        if (response != null && response.isSuccessful) {
            val listOfProducts = response.body()
            Log.d(TAG, "getAllProducts: List of products: $listOfProducts")
            return listOfProducts ?: emptyList()
        }
        return emptyList()
    }

    override suspend fun addProduct(product: Product): ProductResponse? {
        // TODO: Implement this
        return null
    }

    private fun getRetrofitInstance(): ProductAPI {
        return SwipeNetwork.getRetrofitInstance(NetworkConstants.PRODUCT_BASE_URL)!!.create(ProductAPI::class.java)
    }
}