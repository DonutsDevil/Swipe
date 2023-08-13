package com.swapnil.myapplication.network.product


import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.swapnil.myapplication.constants.NetworkConstants
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.network.SwipeNetwork
import com.swapnil.myapplication.repository.State
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

/**
 * Server service Implementation
 */
class ProductNetworkServiceImpl @Inject constructor(private val productAPI: ProductAPI) : ProductNetworkService {
    private val TAG = "ProductNetworkServiceIm"

    override suspend fun getAllProducts(): List<Product> {
        val response = productAPI.getAllProducts()
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

    override suspend fun addProductWithFile(
        product: Product,
        imageUri: Uri
    ): State<ProductResponse> {
        val requestBody = getProductRequestBodyParts(product)

        val productNameRequestBody = requestBody[0]
        val productTypeRequestBody = requestBody[1]
        val priceRequestBody = requestBody[2]
        val taxRequestBody = requestBody[3]

        val file = File(imageUri.path)
        Log.d(TAG, "addProductWithFile: uri path: ${imageUri.path}")
        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        Log.d(TAG, "addProductWithFile: requestFile: $requestFile")
        val imagePart = MultipartBody.Part.createFormData("files[]", file.name, requestFile)
        Log.d(TAG, "addProductWithFile: imagePart: $imagePart")

        val response = productAPI.createProduct(
            productNameRequestBody,
            productTypeRequestBody,
            priceRequestBody,
            taxRequestBody,
            imagePart
        )

        return processResponse(response)
    }

    override suspend fun addProductWithoutFile(product: Product): State<ProductResponse> {
        val requestBody = getProductRequestBodyParts(product)
        val productNameRequestBody = requestBody[0]
        val productTypeRequestBody = requestBody[1]
        val priceRequestBody = requestBody[2]
        val taxRequestBody = requestBody[3]

        val response = productAPI.createProduct(
            productNameRequestBody,
            productTypeRequestBody,
            priceRequestBody,
            taxRequestBody,
        )

        return processResponse(response)
    }

    private fun getRetrofitInstance(
        forceCreation: Boolean = false,
        gson: Gson? = null
    ): ProductAPI {
        return SwipeNetwork.getRetrofitInstance(NetworkConstants.PRODUCT_BASE_URL, gson, forceCreation)!!
            .create(ProductAPI::class.java)
    }

    private fun <T> processResponse(response: Response<T?>?): State<T> {
        return response?.let { _response ->
            if (_response.isSuccessful) {
                _response.body()?.let { _responseBody ->
                    Log.d(TAG, "processResponse: Success: $_responseBody")
                    State.Success(_responseBody)
                } ?: State.Error(_response.message())
            } else {
                Log.d(TAG, "processResponse: Failure: ${_response.errorBody()?.toString()}")
                State.Error(_response.errorBody()?.toString() ?: "Something went wrong when processing request")
            }
        } ?: State.Error("Something went wrong when processing request")
    }

    /**
     * Creates a array which contents request body for posting product details to server
     * @return array of RequestBody where
     *         0th position = name
     *         1st position = type
     *         2nd position = price
     *         3rd position = tax
     */
    private fun getProductRequestBodyParts(product: Product): Array<RequestBody> {
        return arrayOf(
            RequestBody.create(MediaType.parse("text/plain"), product.name!!),
            RequestBody.create(MediaType.parse("text/plain"), product.type!!),
            RequestBody.create(MediaType.parse("text/plain"), product.price!!.toString()),
            RequestBody.create(MediaType.parse("text/plain"), product.tax!!.toString())
        )
    }
}