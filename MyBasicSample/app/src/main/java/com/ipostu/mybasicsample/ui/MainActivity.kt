package com.ipostu.mybasicsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.model.Product

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = ProductListFragment()
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, fragment, ProductListFragment.TAG).commit();
    }

    /** Shows the product detail fragment  */
    fun show(product: Product) {
        val productFragment: ProductFragment = ProductFragment.forProduct(product.getId())

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("product")
            .replace(
                R.id.fragment_container,
                productFragment, null
            ).commit()
    }
}