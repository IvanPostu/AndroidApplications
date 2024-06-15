package com.ipostu.thenewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ipostu.thenewsapp.repository.NewsRepository

class NewsViewModelProviderFactory(
    private val app: Application,
    private val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.cast(NewsViewModel(app, newsRepository)) as T
    }
}