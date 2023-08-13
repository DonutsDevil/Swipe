package com.swapnil.myapplication.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.swapnil.myapplication.R
import com.swapnil.myapplication.repository.State
import com.swapnil.myapplication.utils.ProductAdapter
import com.swapnil.myapplication.utils.hideLoadingDialog
import com.swapnil.myapplication.utils.showLoadingDialog
import com.swapnil.myapplication.viewmodel.ProductViewModel
import com.swapnil.myapplication.viewmodel.ProductViewModelFactory
import javax.inject.Inject

class ProductListingFragment : Fragment() {

    private lateinit var productViewMode: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    private lateinit var rvProductList: RecyclerView
    private lateinit var swipeRefreshListing: SwipeRefreshLayout
    private lateinit var swipeRefreshLayoutError: SwipeRefreshLayout

    private var progressBarDialog: Dialog? = null
    private lateinit var ivError: ImageView

    private lateinit var fabAddProduct: FloatingActionButton

    @Inject
    lateinit var productViewModelFactory: ProductViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_listing, container, false)
        (view.context.applicationContext as SwipeApplication).component.inject(this)
        productViewMode = ViewModelProvider(
            requireActivity(),
            productViewModelFactory
        )[ProductViewModel::class.java]
        initView(view)
        setUpProductListRv()
        fabAddProduct.setOnClickListener {
            it.findNavController().navigate(R.id.action_productListingFragment_to_addProductFragment)
        }
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
                    swipeRefreshListing.isRefreshing = false
                    productAdapter.submitList(state.data)
                    productListingSuccess()
                }
                is State.Error -> {
                    swipeRefreshLayoutError.isRefreshing = false
                    productListingError()
                }
                is State.Reset -> {
                    // do nothing
                }
            }

        }
        productViewMode.getAllProducts(requireContext())
        refreshProductList(swipeRefreshListing)
        refreshProductList(swipeRefreshLayoutError)
        }

    private fun initView(view: View) {
        rvProductList = view.findViewById(R.id.rv_productList)
        swipeRefreshListing = view.findViewById(R.id.swipeRefreshLayout_listing)
        swipeRefreshLayoutError = view.findViewById(R.id.swipeRefreshLayout_error)
        ivError = view.findViewById(R.id.iv_error)
        fabAddProduct = view.findViewById(R.id.fab_add_product)
    }

    private fun setUpProductListRv() {
        productAdapter = ProductAdapter()
        rvProductList.layoutManager = LinearLayoutManager(requireContext())
        rvProductList.adapter = productAdapter
    }

    private fun productListLoading() {
        if (progressBarDialog?.isShowing != true && !swipeRefreshListing.isRefreshing) {
            progressBarDialog = showLoadingDialog(requireContext())
        }
        ivError.visibility = View.GONE
        swipeRefreshLayoutError.visibility = View.GONE
    }

    private fun productListingSuccess() {
        hideLoadingDialog(progressBarDialog)
        ivError.visibility = View.GONE
        if (swipeRefreshListing.visibility != View.VISIBLE) {
            swipeRefreshListing.visibility = View.VISIBLE
        }
        if (rvProductList.visibility != View.VISIBLE) {
            rvProductList.visibility = View.VISIBLE
        }
    }

    private fun productListingError() {
        hideLoadingDialog(progressBarDialog)
        rvProductList.visibility = View.GONE
        swipeRefreshListing.visibility = View.GONE
        if (swipeRefreshLayoutError.visibility != View.VISIBLE) {
            swipeRefreshLayoutError.visibility = View.VISIBLE
        }
        swipeRefreshLayoutError.visibility = View.VISIBLE
        if (ivError.visibility != View.VISIBLE) {
            ivError.visibility = View.VISIBLE
        }
    }

    private fun refreshProductList(swipeLayout: SwipeRefreshLayout) {
        swipeLayout.setOnRefreshListener {
            productViewMode.getAllProducts(requireContext())
        }
    }
}