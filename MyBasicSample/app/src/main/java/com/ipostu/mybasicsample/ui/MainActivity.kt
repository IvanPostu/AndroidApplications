package com.ipostu.mybasicsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ipostu.mybasicsample.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFirstCreation: Boolean = savedInstanceState == null;
        if (isFirstCreation) {
            val fragment = ProductListFragment()
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, ProductListFragment.TAG)
                .commit();
        }
    }
}