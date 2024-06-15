package com.ipostu.thenewsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ipostu.thenewsapp.databinding.FragmentArticleBinding
import com.ipostu.thenewsapp.ui.NewsActivity
import com.ipostu.thenewsapp.ui.NewsViewModel

class ArticleFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var binding: FragmentArticleBinding

    private val args: ArticleFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as NewsActivity).newsViewModel
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        binding.fab.setOnClickListener {
            newsViewModel.addToFavorites(article)
            Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_SHORT).show()
        }
    }

}