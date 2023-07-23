package com.swapnil.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.repository.ProductRepository
import com.swapnil.myapplication.repository.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel() {

    private val _productList = MutableLiveData<State<List<Product>>>()
    val productList: LiveData<State<List<Product>>>
        get() = _productList

    var productName = ""
    var productType = ""
    var productPrice = ""
    var productTax = ""
    var productImageUri: Uri? =null

    fun getAllProducts(context: Context) {
        _productList.postValue(State.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val productState = productRepository.getAllProducts(context)
            _productList.postValue(productState)
        }
    }

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