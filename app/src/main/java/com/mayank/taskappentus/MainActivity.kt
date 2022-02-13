package com.mayank.taskappentus

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mayank.taskappentus.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.mayank.taskappentus.roomDB.ImageDatabase
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    lateinit var viewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var imageAdapter: ImageAdapter
    lateinit var imagesDisplayAdapter: ImagesDisplayAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        create instance of DB
        val dao = ImageDatabase.getInstance(this).imageDatabaseDao
//        create factory for viewModel
        val factory = MainViewModelFactory(dao, this)
//        object of viewmodel class
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.viewmodel = viewModel
//        shimmer effect
        binding.shimmerLayout.startShimmer();
//        object of image adapter for offline case recyclerview
        imageAdapter = ImageAdapter(this)
//        object of image adapter for online case recyclerview
        imagesDisplayAdapter = ImagesDisplayAdapter(this)
//        handle light and darkode
        binding.switchMode.setOnCheckedChangeListener(this)
//        check network connectivity
        if (ConnectivityUtil.isConnected(this)) {
//            when true-> get Data from online case
            setupList()
            setupView()
        } else {
//            when false-> get data from DB
            getDataWhenNoInternet()
        }
//        get recycleView last item fully visible
        getLastItem()
    }

    private fun getLastItem() {
        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()
                val endHasBeenReached = lastVisible + 5 >= totalItemCount
                if (totalItemCount > 0 && endHasBeenReached) {
//any task perform when user reached last of recyclerview item
                }
            }
        })
    }

    private fun getDataWhenNoInternet() {
//        set adapter in recycleview
        binding.rvImages.adapter = imageAdapter
//        observe data for display local data
        viewModel.allImages.observe(this, Observer {

            if (it.isNotEmpty()) {
//                wait 1 sec for sow shimmer effect
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.shimmerLayout.stopShimmer();
                    binding.shimmerLayout.visibility = View.GONE;
                }, 1000)
//                set data on adpater
                retrieveList(it)
            } else {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun retrieveList(users: List<ImageModel>) {
        imageAdapter.apply {
            loadImage(users)
            notifyDataSetChanged()
        }
    }

    private fun setupList() {
//        set adapter on recycler view
//        "apply" feature provide view properties
        binding.rvImages.apply {
            adapter = imagesDisplayAdapter.withLoadStateFooter(
//                set loader effect on bottom of recyclerview when single page data display properly
                footer = ImagesLoadStateAdapter { imagesDisplayAdapter.retry() }
            )
            setHasFixedSize(true)
        }
    }

    private fun setupView() {
        //                wait 1 sec for sow shimmer effect
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerLayout.stopShimmer();
            binding.shimmerLayout.visibility = View.GONE;
        }, 1000)
//        get data from api using paging 3
        lifecycleScope.launch {
            viewModel.images.collectLatest { pagedData ->
                imagesDisplayAdapter.submitData(pagedData)
            }
        }
    }
//override method for check switch compact true or false
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
//            dark mode on
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
//            dark mode off
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}