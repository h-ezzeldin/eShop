package com.ezz.eshop.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezz.eshop.adapters.ReviewAdapter
import com.ezz.eshop.databinding.ActivityReviewBinding
import com.ezz.eshop.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPrefs: SharedPreferences
    private lateinit var b: ActivityReviewBinding
    private val adapter = ReviewAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(b.root)
        getCart()
        b.productsRecycler.layoutManager = LinearLayoutManager(this)
        b.productsRecycler.adapter = adapter

        b.checkOut.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
            finish()
        }
    }

    private fun getCart() {
        val cartString = sharedPrefs.getString("cart", "")
        if (cartString != "") {
            val cart = fromJsonTOList(cartString!!)
            adapter.updateList(cart.toMutableList())

            var totalOfTotal = 0.0
            cart.forEach {
                totalOfTotal += it.price!!.times(it.quantity!!)
            }

            b.totalTotal.text = "Total: ${totalOfTotal} EGP"
        }
    }

    private fun fromJsonTOList(json: String): List<Product> {
        val itemType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, itemType)
    }
}
