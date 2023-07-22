package com.swapnil.myapplication.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("image")
    val images: List<String>? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("product_name")
    val name: String? = null,
    @SerializedName("product_type")
    val type: String? = null,
    @SerializedName("tax")
    val tax: Double? = null
)
