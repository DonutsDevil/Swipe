package com.swapnil.myapplication.local.product

import android.content.Context
import com.swapnil.myapplication.model.Product
import javax.inject.Inject

/**
 * Local DB service Implementation
 */
class ProductLocalServiceImpl @Inject constructor(private val productDao: ProductDao): ProductLocalService {

    override suspend fun addProducts(productList: List<Product>, context: Context) {
        productDao.insertList(productList)
    }

    override suspend fun deleteAllProducts(context: Context) {
        productDao.deleteAllProducts()
    }

    override suspend fun getAll(context: Context): List<Product> {
        return productDao.getAll()
    }
}