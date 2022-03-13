package com.ezz.eshop.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ezz.eshop.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repo: ProductsRepository) : ViewModel() {
    private val TAG = "tag ProductsViewModel"

    private val productsListMLD = MutableLiveData<List<Product>>().also { it.value = listOf() }
    val productsListLD: LiveData<List<Product>> = productsListMLD

    private val productMLD = MutableLiveData<Product>()
    val productLD: LiveData<Product> = productMLD

    val loading = MutableLiveData<Boolean>().also { it.value = true }
    val errorMessage = MutableLiveData<String>()

    private var job: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        errorMessage.postValue("API Error!")
        loading.postValue(false)
    }

    fun getAllProducts() {
        loading.postValue(true)
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response = repo.getAllProducts()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productsListMLD.postValue(response.body())
                    loading.postValue(false)
                    Log.d(TAG, "success: ${response.body()}")
                } else {
                    loading.postValue(false)
                    errorMessage.postValue(response.message())
                    Log.d("TAG", "error: ${response.message()}")
                }
            }
        }
    }

    fun getProductById(id: Int) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response = repo.getProductById(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productMLD.postValue(response.body())
                    loading.postValue(false)
                    Log.d(TAG, "success: ${response.body()}")
                } else {
                    loading.postValue(false)
                    errorMessage.postValue(response.message())
                    Log.d("TAG", "error: ${response.message()}")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
