package com.swapnil.myapplication.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.swapnil.myapplication.R
import com.swapnil.myapplication.network.product.ProductNetworkServiceImpl
import com.swapnil.myapplication.repository.ProductRepository
import com.swapnil.myapplication.viewmodel.ProductViewModel
import com.swapnil.myapplication.viewmodel.ProductViewModelFactory

private const val TAG = "ProductListingFragment"
class ProductListingFragment : Fragment() {

    lateinit var productViewMode: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_listing, container, false)
        productViewMode = ViewModelProvider(requireActivity(), ProductViewModelFactory(ProductRepository(ProductNetworkServiceImpl())))[ProductViewModel::class.java]
        return view
    }

    override fun onResume() {
        super.onResume()
        productViewMode.productList.observe(this) {
            Log.d(TAG, "onResume: List is: $it")
        }
    }
}