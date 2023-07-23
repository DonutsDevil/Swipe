package com.swapnil.myapplication.local.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.swapnil.myapplication.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    suspend fun getAll(): List<Product>

    @Insert
    suspend fun insertList(productList: List<Product>)

    @Query("DELETE FROM Product")
    suspend fun deleteAllProducts()
}
