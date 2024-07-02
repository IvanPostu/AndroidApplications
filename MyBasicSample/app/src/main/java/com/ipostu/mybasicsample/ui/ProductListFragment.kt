package com.ipostu.mybasicsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.databinding.FragmentProductListBinding
import com.ipostu.mybasicsample.viewmodel.ProductListViewModel

class ProductListFragment : Fragment() {
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        android.view.View.INVISIBLE
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);

        // Set the value for isLoading
        binding.setIsLoading(true); // Replace with your string value

        // Set the lifecycle owner for LiveData updates (if applicable)
        binding.setLifecycleOwner(this);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: ProductListViewModel =
            ViewModelProvider(this).get(ProductListViewModel::class.java)
    }

    companion object {
        val TAG: String = "ProductListFragment"
    }
}