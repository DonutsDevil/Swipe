package com.swapnil.myapplication.viewmodel

import android.content.Context
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

    fun getAllProducts(context: Context) {
        _productList.postValue(State.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            val productState = productRepository.getAllProducts(context)
            _productList.postValue(productState)
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