package com.swapnil.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("image")
    val images: String? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("product_name")
    val name: String? = null,
    @SerializedName("product_type")
    val type: String? = null,
    @SerializedName("tax")
    val tax: Double? = null,
)
