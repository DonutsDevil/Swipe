package com.swapnil.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.*
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.model.ProductResponse
import com.swapnil.myapplication.repository.AddProductErrors
import com.swapnil.myapplication.repository.ProductRepository
import com.swapnil.myapplication.repository.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val TAG = "ProductViewModel"
    private val _productList = MutableLiveData<State<List<Product>>>()
    val productList: LiveData<State<List<Product>>>
        get() = _productList

    private val _addProduct = MutableLiveData<State<ProductResponse>>()
    val addProduct: LiveData<State<ProductResponse>>
        get() = _addProduct

    var productName = ""
    var productType = ""
    var productPrice = ""
    var productTax = ""
    var productImageUri: Uri? = null

    fun getAllProducts(context: Context) {
        _productList.postValue(State.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val productState = productRepository.getAllProducts(context)
            _productList.postValue(productState)
        }
    }

    fun addProduct(name: String?=null, type: String? = null, price: String? = null, tax: String? = null) {
        _addProduct.postValue(State.Loading())
        Log.d(TAG, "addProduct: name: $name, type: $type, price: $price, tax: $tax, imageUri: $productImageUri")
        val areFieldsValid = areProductAdditionFieldsValid(name, type, price, tax)
        Log.d(TAG, "addProduct: Fields are Valid? $areFieldsValid")
        if (!areFieldsValid) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val product = Product(name = name, price = price?.toDouble(), type = type, tax = tax?.toDouble())
            val productResponseState = productRepository.addProduct(product, productImageUri)
            _addProduct.postValue(productResponseState)
        }
    }

    /**
     * Validates Product name, type, price, tax and returns true or false.
     */
    private fun areProductAdditionFieldsValid(name: String?=null, type: String? = null, price: String? = null, tax: String? = null): Boolean {
        if (TextUtils.isEmpty(name)) {
            _addProduct.postValue(State.Error(AddProductErrors.NAME.name))
            return false
        }
        if (TextUtils.isEmpty(type)) {
            _addProduct.postValue(State.Error(AddProductErrors.TYPE.name))
            return false
        }
        if (TextUtils.isEmpty(price)) {
            _addProduct.postValue(State.Error(AddProductErrors.PRICE.name))
            return false
        }
        if (TextUtils.isEmpty(tax)) {
            _addProduct.postValue(State.Error(AddProductErrors.TAX.name))
            return false
        }
        return true
    }

    /**
     * Clears [_addProduct] data and makes it to Reset State.
     */
    fun clearAddProductCache() {
        _addProduct.postValue(State.Reset())
    }

    /**
     * Saves values in view model for configuration changes
     */
    fun setProductValues(
        imageUri: Uri? = null,
        name: String? = null,
        type: String? = null,
        price: String? = null,
        tax: String? = null
    ) {
        imageUri?.let {
            productImageUri = it
        }
        name?.let {
            productName = it
        }
        type?.let {
            productType = it
        }
        price?.let {
            productPrice = it
        }
        tax?.let {
            productTax = it
        }
    }
}

class ProductViewModelFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}