package com.ipostu.mybasicsample.ui

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.databinding.ProductItemBinding
import com.ipostu.mybasicsample.model.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    var mProductList: List<Product?>? = null

    private var mProductClickCallback: ProductClickCallback? = null

    constructor(clickCallback: ProductClickCallback?) {
        mProductClickCallback = clickCallback
        setHasStableIds(true)
    }

    fun setProductList(productList: List<Product?>) {
        if (mProductList == null) {
            mProductList = productList
            notifyItemRangeInserted(0, productList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mProductList!!.size
                }

                override fun getNewListSize(): Int {
                    return productList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mProductList!![oldItemPosition]!!.id ===
                            productList[newItemPosition]!!.id
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val newProduct = productList[newItemPosition]
                    val oldProduct = mProductList!![oldItemPosition]!!
                    return (newProduct!!.id === oldProduct.id && TextUtils.equals(
                        newProduct!!.description,
                        oldProduct.description
                    )
                            && TextUtils.equals(
                        newProduct!!.name,
                        oldProduct.name
                    )) && newProduct!!.price === oldProduct.price
                }
            })
            mProductList = productList
            result.dispatchUpdatesTo(this)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ProductItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context), R.layout.product_item,
                parent, false
            )
        binding.callback = mProductClickCallback
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.product = mProductList!![position]
        holder.binding.executePendingBindings()
    }


    override fun getItemCount(): Int {
        return if (mProductList == null) 0 else mProductList!!.size
    }

    override fun getItemId(position: Int): Long {
        return mProductList!![position]!!.id.toLong()
    }

    class ProductViewHolder(binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: ProductItemBinding = binding
    }
}