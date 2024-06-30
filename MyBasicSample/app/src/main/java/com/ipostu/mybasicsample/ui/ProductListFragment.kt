package com.ipostu.mybasicsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.databinding.FragmentProductListBinding

class ProductListFragment : Fragment() {
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);

        // Set the value for isLoading
        binding.setIsLoading("Loading..."); // Replace with your string value

        // Set the lifecycle owner for LiveData updates (if applicable)
        binding.setLifecycleOwner(this);

        return binding.root
    }

    companion object {
        val TAG: String = "ProductListFragment"
    }
}