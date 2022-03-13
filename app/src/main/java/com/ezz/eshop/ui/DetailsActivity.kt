package com.ezz.eshop.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ezz.eshop.databinding.ActivityDetailsBinding
import com.ezz.eshop.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val productsVM by viewModels<ProductsViewModel>()
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private lateinit var b: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(b.root)

// gets product id from intent
        val id = intent.getIntExtra("id", -1)
        if (id > -1) initViewModel(id)

        b.backButton.setOnClickListener {
            onBackPressed()
        }

        b.toCartButton.setOnClickListener {
            addToCart(b.product!!)
        }
    }

    /**
     * initialise viewModel and observers
     */
    private fun initViewModel(id: Int) {
        productsVM.productLD.observe(this) {
            b.product = it

            // load image into imageView
            Glide
                .with(this)
                .load(it.image)
                .override(500, 500)
                .into(b.productImage)
        }

        productsVM.loading.observe(this) {
            b.loading.visibility = if (it) View.VISIBLE else View.GONE
            b.toCartButton.isEnabled = !it
        }

        productsVM.getProductById(id)
    }

    /**
     * adds product to cart
     */
    private fun addToCart(product: Product) {
        product.quantity = 1
        var cart = mutableListOf<Product>()
        val cartString = sharedPrefs.getString("cart", "")
        if (cartString != "") {
            cart = fromJsonTOList(cartString!!).toMutableList()
        }
        // check if product is in cart
        val itemInCart = cart.find { it.id == product.id }
        if (itemInCart == null) {
            cart.add(product)
        } else { // if product in cart increase quantity
            itemInCart.quantity = itemInCart.quantity?.plus(1)
        }
        sharedPrefs.edit().putString("cart", Gson().toJson(cart)).apply()
        Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show()
    }

    /**
     * json to list converter
     */
    private fun fromJsonTOList(json: String): List<Product> {
        val itemType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, itemType)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
