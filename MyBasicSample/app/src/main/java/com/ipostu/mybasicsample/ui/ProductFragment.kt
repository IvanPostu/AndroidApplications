package com.ipostu.mybasicsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.databinding.ProductFragmentBinding
import com.ipostu.mybasicsample.viewmodel.ProductViewModel

class ProductFragment : Fragment() {
    private var _binding: ProductFragmentBinding? = null
    private var _commentAdapter: CommentAdapter? = null
    private val binding get() = _binding!!
    private val commentAdapter get() = _commentAdapter!!

    companion object {
        var KEY_PRODUCT_ID: String = "product_id"

        fun forProduct(productId: Int): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle()
            args.putInt(KEY_PRODUCT_ID, productId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false);


        // Create and set the adapter for the RecyclerView.
        _commentAdapter = CommentAdapter(mCommentClickCallback)
        binding.commentList.setAdapter(commentAdapter)
        return binding.root
    }

    private val mCommentClickCallback = CommentClickCallback { comment -> }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory: ProductViewModel.Factory = ProductViewModel.Factory(
            requireActivity().application, requireArguments().getInt(KEY_PRODUCT_ID)
        )

        val model: ProductViewModel = ViewModelProvider(this, factory)
            .get(ProductViewModel::class.java)

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.setProductViewModel(model)

        subscribeToModel(model)
    }


    private fun subscribeToModel(model: ProductViewModel) {
        // Observe comments
        model.comments.observe(viewLifecycleOwner) { commentEntities ->
            if (commentEntities != null) {
                binding.setIsLoading(false)
                commentAdapter.submitList(commentEntities)
            } else {
                binding.setIsLoading(true)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        _commentAdapter = null
        super.onDestroyView()
    }
}