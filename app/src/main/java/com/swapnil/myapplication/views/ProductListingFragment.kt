package com.swapnil.myapplication.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.myapplication.R
import com.swapnil.myapplication.network.product.ProductNetworkServiceImpl
import com.swapnil.myapplication.repository.ProductRepository
import com.swapnil.myapplication.utils.ProductAdapter
import com.swapnil.myapplication.viewmodel.ProductViewModel
import com.swapnil.myapplication.viewmodel.ProductViewModelFactory

class ProductListingFragment : Fragment() {

    lateinit var productViewMode: ProductViewModel
    lateinit var rvProductList: RecyclerView
    lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_listing, container, false)
        productViewMode = ViewModelProvider(requireActivity(), ProductViewModelFactory(ProductRepository(ProductNetworkServiceImpl())))[ProductViewModel::class.java]
        initView(view)
        setUpProductListRv()
        return view
    }

    override fun onResume() {
        super.onResume()
        productViewMode.productList.observe(this) {
            productAdapter.submitList(it)
        }
        productViewMode.getAllProducts(requireContext())
    }


    private fun initView(view: View) {
        rvProductList = view.findViewById(R.id.rv_productList)
    }

    private fun setUpProductListRv() {
        productAdapter = ProductAdapter()
        rvProductList.layoutManager = LinearLayoutManager(requireContext())
        rvProductList.adapter = productAdapter
    }
}