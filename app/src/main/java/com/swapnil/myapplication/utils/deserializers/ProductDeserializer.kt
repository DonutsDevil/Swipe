package com.swapnil.myapplication.utils.deserializers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.swapnil.myapplication.model.Product
import java.lang.reflect.Type

class ProductDeserializer: JsonDeserializer<Product> {

    /**
     * Used by Gson so that we can convert image from API for [com.swapnil.myapplication.model.Product], where single url and at times an array of url is provided in images
     */
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Product {
        val jsonObject = json?.asJsonObject
        val imagesElement = jsonObject?.get("image")
        if (imagesElement?.isJsonArray == true) {
            // parse image as list
            val images = imagesElement.asJsonArray.map { it.asString }
            return Gson().fromJson(jsonObject, Product::class.java).copy(images = images)
        } else if (imagesElement?.isJsonPrimitive == true) {
            // parse image as a single string
            val image = imagesElement.asString
            return Gson().fromJson(jsonObject, Product::class.java).copy(images = listOf(image))
        }

        // If the "images" field is missing or malformed, treat it as an empty list
        return Gson().fromJson(jsonObject, Product::class.java).copy(images = emptyList())
    }

    /**
     * @return Gson object which is a type converter for [com.swapnil.myapplication.model.Product]
     */
    fun getProductDeserializerGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Product::class.java, this)
            .create()
    }

}