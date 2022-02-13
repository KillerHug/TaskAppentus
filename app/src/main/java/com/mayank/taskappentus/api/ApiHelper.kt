package com.mayank.taskappentus.api



class ApiHelper (private val apiService: ApiService) {
    suspend fun getImage(pageNo: Int) = apiService.getImage(pageNo)
}