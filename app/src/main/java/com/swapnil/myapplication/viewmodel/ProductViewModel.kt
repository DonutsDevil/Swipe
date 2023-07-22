package com.swapnil.myapplication.viewmodel

import androidx.lifecycle.*
import com.swapnil.myapplication.model.Product
import com.swapnil.myapplication.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel() {

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
        get() = _productList

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val productList = productRepository.getAllProducts()
            _productList.postValue(productList)
        }
    }

    fun clearAllProductsCache() {
        _productList.postValue(emptyList())
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