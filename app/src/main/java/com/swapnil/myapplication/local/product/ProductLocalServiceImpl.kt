package com.swapnil.myapplication.local.product

import android.content.Context
import com.swapnil.myapplication.local.SwipeDB
import com.swapnil.myapplication.model.Product

class ProductLocalServiceImpl: ProductLocalService {

    override suspend fun addProducts(productList: List<Product>, context: Context) {
        SwipeDB.getInstance(context).productDao().insertList(productList)
    }

    override suspend fun deleteAllProducts(context: Context) {
        SwipeDB.getInstance(context).productDao().deleteAllProducts()
    }

    override suspend fun getAll(context: Context): List<Product> {
        return SwipeDB.getInstance(context).productDao().getAll()
    }
}