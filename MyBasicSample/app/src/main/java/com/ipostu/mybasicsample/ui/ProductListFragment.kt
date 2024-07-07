package com.ipostu.mybasicsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.databinding.FragmentProductListBinding
import com.ipostu.mybasicsample.db.entity.ProductEntity
import com.ipostu.mybasicsample.viewmodel.ProductListViewModel

class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private var _productAdapter: ProductAdapter? = null
    private val binding get() = _binding!!
    private val productAdapter get() = _productAdapter!!

    companion object {
        val TAG: String = "ProductListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);
        _productAdapter = ProductAdapter(mProductClickCallback)
        binding.productsList.setAdapter(productAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: ProductListViewModel =
            ViewModelProvider(this).get(ProductListViewModel::class.java)

        binding.productsSearchBtn.setOnClickListener { v ->
            val query: String = binding.productsSearchBox.text?.toString().orEmpty()
            viewModel.setQuery(query)
        }

        subscribeUi(viewModel.products)
    }

    private fun subscribeUi(liveData: LiveData<List<ProductEntity?>>) {
        // Update the list when the data changes
        liveData.observe(
            viewLifecycleOwner
        ) { myProducts: List<ProductEntity?>? ->
            if (myProducts != null) {
                binding.setIsLoading(false)
                productAdapter.setProductList(myProducts)
            } else {
                binding.setIsLoading(true)
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            binding.executePendingBindings()
        }
    }

    override fun onDestroyView() {
        _binding = null
        _productAdapter = null
        super.onDestroyView()
    }

    private val mProductClickCallback = ProductClickCallback { product ->
        if (super.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (requireActivity() as MainActivity).show(product)
        }
    }
}