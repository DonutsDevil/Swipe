package com.swapnil.myapplication.network.product

import android.content.Context
import android.net.Uri
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.repository.State

/**
 * Service use for communicating to the server
 */
interface ProductNetworkService {

    /**
     * Get All the products from the server
     * @return List of [com.swapnil.myapplication.model.Product]
     */
    suspend fun getAllProducts(): List<Product>

    /**
     * Load all images from the [urlsList] locally and saves it in the disk
     * @param urlsList: List of images url
     */
    suspend fun loadAllImages(urlsList: List<String?>, context: Context)

    /**
     * Saves product with file information on the server
     * @param product: details required by server to save
     * @param imageUri: a uri which refers to image
     *
     * @return [com.swapnil.myapplication.repository.State] response
     */
    suspend fun addProductWithFile(
        product: Product,
        imageUri: Uri
    ): State<ProductResponse>

    /**
     * Saved product information on the server
     * @param product: details required by server to save
     *
     * @return [com.swapnil.myapplication.repository.State] response
     */
    suspend fun addProductWithoutFile(product: Product): State<ProductResponse>
}