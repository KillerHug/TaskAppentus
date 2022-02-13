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

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: ItemImageBinding = DataBindingUtil.bind(itemView.rootView)!!
        fun bind(item: ImageModel) {
            /* Glide.with(_context)
                 .load(user.download_url)
 //                .placeholder(R.drawable.loading)
                 .into(itemBinding!!.ivItem)*/
            itemBinding.shimmerLayout.visibility = View.VISIBLE
            itemBinding.shimmerLayout.startShimmer()
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
                        itemBinding.shimmerLayout.stopShimmer()
                        itemBinding.shimmerLayout.visibility = View.GONE
                        return false
                    }

                })
                .into(itemBinding.ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        Log.e("TAG", "onBindViewHolder: " + imageList[position].download_url)
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(temp: ArrayList<ImageModel>) {
        this.imageList.apply {
            clear()
            addAll(temp)
        }
        notifyDataSetChanged()
    }
}