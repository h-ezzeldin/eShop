package com.ezz.eshop.ui

import com.ezz.eshop.api.ApiService
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getAllProducts() = apiService.getAllProducts()

    suspend fun getProductById(id: Int) = apiService.getProductById(id)


}
