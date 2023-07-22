package com.swapnil.myapplication.network.product


import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.swapnil.myapplication.constants.NetworkConstants
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.network.SwipeNetwork
import kotlinx.coroutines.*


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

    override suspend fun loadAllImages(urlsList: List<String?>, context: Context) {
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        val imageUrlDeferredList = ArrayList<Deferred<*>>()
        val scope = CoroutineScope(Dispatchers.IO)
        urlsList.forEach { url ->
            imageUrlDeferredList.add(scope.async {
                loadImage(url, requestOptions, context)
            })
        }
        imageUrlDeferredList.awaitAll()
    }

    private fun loadImage(url: String?, requestOptions: RequestOptions, context: Context) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        Glide.with(context)
            .load(url)
            .apply(requestOptions)
            .preload()
    }

    override suspend fun addProduct(product: Product): ProductResponse? {
        // TODO: Implement this
        return null
    }

    private fun getRetrofitInstance(): ProductAPI {
        return SwipeNetwork.getRetrofitInstance(NetworkConstants.PRODUCT_BASE_URL)!!.create(ProductAPI::class.java)
    }
}