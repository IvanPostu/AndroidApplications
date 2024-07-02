package com.ipostu.mybasicsample.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle

class ProductListViewModel(application: Application, val savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {

    private val QUERY_KEY = "QUERY"



}