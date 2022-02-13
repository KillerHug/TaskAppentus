package com.mayank.taskappentus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mayank.taskappentus.databinding.ItemLoadingStateBinding

class ImagesLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ImagesLoadStateAdapter.PassengerLoadStateViewHolder>() {
//    display loader on bottom of screen
    inner class PassengerLoadStateViewHolder(private val binding: ItemLoadingStateBinding,
                                             private val retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
//            check load state for display progressbar
            when (loadState) {
                LoadState.Loading -> binding.progressbar.visibility = View.VISIBLE
                else -> binding.progressbar.visibility = View.GONE
            }
            binding.progressbar.visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: PassengerLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = PassengerLoadStateViewHolder(
        ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
}