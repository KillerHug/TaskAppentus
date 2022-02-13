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
import com.mayank.taskappentus.api.Status
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
        val dao = ImageDatabase.getInstance(this).imageDatabaseDao
        val factory = MainViewModelFactory(dao, this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.viewmodel = viewModel
        binding.shimmerLayout.startShimmer();
        imageAdapter = ImageAdapter(this)
        imagesDisplayAdapter = ImagesDisplayAdapter(this)
        binding.switchMode.setOnCheckedChangeListener(this)
        if (ConnectivityUtil.isConnected(this)) {
            setupList()
            setupView()
        } else {
            getDataWhenNoInternet()
        }
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

                }
            }
        })
    }

    private fun getDataWhenNoInternet() {
        binding.rvImages.adapter = imageAdapter
        viewModel.allImages.observe(this, Observer {
            if (it.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.shimmerLayout.stopShimmer();
                    binding.shimmerLayout.visibility = View.GONE;
                }, 1000)
                retrieveList(it)
            } else {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initObserver() {
        binding.rvImages.adapter = imageAdapter
        viewModel.getImage().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users -> retrieveList(users) }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<ImageModel>) {
//        list.clear()
        imageAdapter.apply {
            loadImage(users)
            notifyDataSetChanged()
            Log.e("TAG", "retrieveList: ${users.size}")
//            list.addAll(users)
        }
    }

    private fun setupList() {
        binding.rvImages.apply {
            adapter = imagesDisplayAdapter.withLoadStateFooter(
                footer = ImagesLoadStateAdapter { imagesDisplayAdapter.retry() }
            )
            setHasFixedSize(true)
        }
    }

    private fun setupView() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.shimmerLayout.stopShimmer();
            binding.shimmerLayout.visibility = View.GONE;
        }, 1000)
        lifecycleScope.launch {
            viewModel.images.collectLatest { pagedData ->
                imagesDisplayAdapter.submitData(pagedData)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

//
}