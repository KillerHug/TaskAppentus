package com.mayank.taskappentus

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mayank.taskappentus.databinding.ItemImageBinding
import java.util.*

class ImageAdapter(context: Context) :
    RecyclerView.Adapter<ImageAdapter.DataViewHolder>() {
    var imageList = ArrayList<ImageModel>()
    var _context = context

//    recyclerview adapter viewholder for display item
    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: ItemImageBinding = DataBindingUtil.bind(itemView.rootView)!!
        fun bind(item: ImageModel) {
            itemBinding.shimmerLayout.visibility = View.VISIBLE
//            shimmer start for single item
            itemBinding.shimmerLayout.startShimmer()
//            display image on view using glide
            Glide.with(_context)
                .load(item.download_url)
//              glide cache storage
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
//                        manage shimmering effect
                        itemBinding.shimmerLayout.visibility = View.VISIBLE
                        itemBinding.shimmerLayout.startShimmer()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //                        manage shimmering effect
                        itemBinding.shimmerLayout.stopShimmer()
                        itemBinding.shimmerLayout.visibility = View.GONE
                        return false
                    }

                })
                .into(itemBinding.ivItem)
        }
    }

//    create view for adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))

//    get item size
    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    fun loadImage(list: List<ImageModel>) {
        this.imageList.clear()
        this.imageList.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }
}