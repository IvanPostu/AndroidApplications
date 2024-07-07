package com.ipostu.mybasicsample.ui

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ipostu.mybasicsample.R
import com.ipostu.mybasicsample.databinding.CommentItemBinding
import com.ipostu.mybasicsample.db.entity.CommentEntity

class CommentAdapter : ListAdapter<CommentEntity, CommentAdapter.CommentViewHolder> {
    private var mCommentClickCallback: CommentClickCallback? = null

    companion object {
        private var diffCallback: AsyncDifferConfig<CommentEntity> =
            AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<CommentEntity>() {
                override fun areItemsTheSame(
                    old: CommentEntity,
                    comment: CommentEntity
                ): Boolean {
                    return old.id === comment.id
                }

                override fun areContentsTheSame(
                    old: CommentEntity,
                    comment: CommentEntity
                ): Boolean {
                    return old.id === comment.id
                            && old.postedAt.equals(comment.postedAt)
                            && old.productId === comment.productId
                            && TextUtils.equals(
                        old.text, comment.text
                    )
                }
            }).build()
    }

    constructor(commentClickCallback: CommentClickCallback?) : super(diffCallback) {
        mCommentClickCallback = commentClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding: CommentItemBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(parent.context), R.layout.comment_item,
                parent, false
            )
        binding.callback = mCommentClickCallback
        return CommentViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment = getItem(position)
        holder.binding.executePendingBindings()
    }

    class CommentViewHolder(binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: CommentItemBinding = binding
    }
}