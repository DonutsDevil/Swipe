package com.swapnil.myapplication.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("product_details")
    val product_details: Product,
    @SerializedName("product_id")
    val product_id: Int,
    @SerializedName("success")
    val success: Boolean
)