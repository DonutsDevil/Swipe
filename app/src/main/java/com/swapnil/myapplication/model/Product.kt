package com.swapnil.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

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
    val tax: Double? = null,
    @Expose(serialize = false, deserialize = false)
    val file: File? = null // only used when doing post request to the product.
)
