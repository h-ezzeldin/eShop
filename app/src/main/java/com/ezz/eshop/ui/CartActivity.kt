package com.ezz.eshop.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezz.eshop.adapters.CartAdapter
import com.ezz.eshop.databinding.ActivityCartBinding
import com.ezz.eshop.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    private lateinit var b: ActivityCartBinding

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private var adapter = CartAdapter()
    private var cart = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCartBinding.inflate(layoutInflater)
        setContentView(b.root)

        getCart()

        // initialise RecyclerView
        b.productsRecycler.layoutManager = LinearLayoutManager(this)
        b.productsRecycler.adapter = adapter

        // initialise back button
        b.topAppBar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }

        // initialise review button
        b.reviewFab.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
            finish()
        }
    }

    /**
     * gets cart products from shared preferences
     */
    private fun getCart() {
        val cartString = sharedPrefs.getString("cart", "")
        if (cartString != "") { // checks if cart not empty
            cart = fromJsonTOList(cartString!!)
            adapter.updateList(cart.toMutableList())
        }
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
