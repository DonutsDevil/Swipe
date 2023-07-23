package com.swapnil.myapplication.local.product

import android.content.Context
import com.swapnil.myapplication.model.Product

/**
 * Service use for saving products locally
 */
interface ProductLocalService {

    suspend fun addProducts(productList: List<Product>, context: Context)

    suspend fun deleteAllProducts(context: Context)

    suspend fun getAll(context: Context): List<Product>
}