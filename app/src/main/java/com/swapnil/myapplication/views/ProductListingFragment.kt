package com.swapnil.myapplication.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.myapplication.R
import com.swapnil.myapplication.network.product.ProductNetworkServiceImpl
import com.swapnil.myapplication.repository.ProductRepository
import com.swapnil.myapplication.repository.State
import com.swapnil.myapplication.utils.ProductAdapter
import com.swapnil.myapplication.viewmodel.ProductViewModel
import com.swapnil.myapplication.viewmodel.ProductViewModelFactory

class ProductListingFragment : Fragment() {

    lateinit var productViewMode: ProductViewModel
    lateinit var productAdapter: ProductAdapter

    lateinit var rvProductList: RecyclerView

    lateinit var progressBar: ProgressBar
    lateinit var ivError: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_listing, container, false)
        productViewMode = ViewModelProvider(
            requireActivity(),
            ProductViewModelFactory(ProductRepository(ProductNetworkServiceImpl()))
        )[ProductViewModel::class.java]
        initView(view)
        setUpProductListRv()
        return view
    }

    override fun onResume() {
        super.onResume()
        productViewMode.productList.observe(this) { state ->
            when (state) {
                is State.Loading -> {
                    productListLoading()
                }
                is State.Success -> {
                    productAdapter.submitList(state.data)
                    productListingSuccess()
                }
                is State.Error -> {
                    productListingError()
                }
            }

        }
        productViewMode.getAllProducts(requireContext())
    }

    private fun initView(view: View) {
        rvProductList = view.findViewById(R.id.rv_productList)
        progressBar = view.findViewById(R.id.progressBar)
        ivError = view.findViewById(R.id.iv_error)
    }

    private fun setUpProductListRv() {
        productAdapter = ProductAdapter()
        rvProductList.layoutManager = LinearLayoutManager(requireContext())
        rvProductList.adapter = productAdapter
    }

    private fun productListLoading() {
        if (progressBar.visibility != View.VISIBLE) {
            progressBar.visibility = View.VISIBLE
        }
        ivError.visibility = View.GONE
    }

    private fun productListingSuccess() {
        progressBar.visibility = View.GONE
        ivError.visibility = View.GONE
        if (rvProductList.visibility != View.VISIBLE) {
            rvProductList.visibility = View.VISIBLE
        }
    }

    private fun productListingError() {
        progressBar.visibility = View.GONE
        rvProductList.visibility = View.GONE
        if (ivError.visibility != View.VISIBLE) {
            ivError.visibility = View.VISIBLE
        }
    }
}