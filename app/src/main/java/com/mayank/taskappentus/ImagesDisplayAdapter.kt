package com.mayank.taskappentus

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.DataSource
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.mayank.taskappentus.databinding.ItemImageBinding

class ImagesDisplayAdapter(context: MainActivity) :
    PagingDataAdapter<ImageModel, ImagesDisplayAdapter.PassengersViewHolder>(PassengersComparator) {
    var _context = context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PassengersViewHolder {
        return PassengersViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PassengersViewHolder, position: Int) {
//        get model data from list
        val item = getItem(position)
        //        display data on view
        item?.let { holder.bindPassenger(it) }
    }

    inner class PassengersViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindPassenger(item: ImageModel) = with(binding) {
            binding.shimmerLayout.visibility = View.VISIBLE
//            shimmer effect start
            binding.shimmerLayout.startShimmer()
//            image display
            Glide.with(_context)
                .load(item.download_url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //                        manage shimmer effect
                        binding.shimmerLayout.visibility = View.VISIBLE
                        binding.shimmerLayout.startShimmer()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
//                        manage shimmer effect
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.ivItem)
        }
    }
}

//    check duplicate item
object PassengersComparator : DiffUtil.ItemCallback<ImageModel>() {
    override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem == newItem
    }
}