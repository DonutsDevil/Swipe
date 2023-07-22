package com.swapnil.myapplication.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.myapplication.R
import com.swapnil.myapplication.model.Product

class ProductAdapter: ListAdapter<Product, ProductAdapter.Companion.Holder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindProduct(getItem(position))
    }

    companion object {
        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val productType: TextView
            private val productName: TextView
            private val productPrice: TextView
            private val productTax: TextView

            init {
                productType = itemView.findViewById(R.id.tv_product_type)
                productName = itemView.findViewById(R.id.tv_product_name)
                productPrice = itemView.findViewById(R.id.tv_product_price)
                productTax = itemView.findViewById(R.id.tv_product_tax)

            }

            fun bindProduct(product: Product) {
                productType.text = product.type
                productName.text = product.name
                productPrice.text = itemView.context.getString(R.string.product_price, String.format("%.2f", product.price))
                productTax.text = itemView.context.getString(R.string.product_tax, String.format("%.2f", product.tax))
            }
        }
    }
}


class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
