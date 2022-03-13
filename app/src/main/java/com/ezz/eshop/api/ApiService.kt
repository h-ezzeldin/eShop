package com.ezz.eshop.api


import com.ezz.eshop.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("products/")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

//    @GET("carts/1")
//    suspend fun getAllCartItems(): Response<Cart>
//
//    @PATCH("carts/1")
//    suspend fun updateItemInCart(@Body cartProduct: CartProduct)
//
//    @PUT("carts/1")
//    suspend fun putItemToCart(@Body cartProduct: CartProduct)
}
