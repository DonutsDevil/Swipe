package com.swapnil.myapplication.repository

/**
 * Responsible for showing UI elements
 */
sealed class State<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T>() : State<T>()
    class Success<T>(data: T) : State<T>(data = data)
    class Error<T>(errorMessage: String) : State<T>(errorMessage = errorMessage)
    class Reset<T>() : State<T>() // used for clearing previous States
}

enum class AddProductErrors(val errorMessage: String) {
    NAME("Name is mandatory"),
    TYPE("Type is mandatory"),
    PRICE("Price is mandatory"),
    TAX("Tax is mandatory")
}