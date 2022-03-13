package com.ezz.eshop.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ezz.eshop.R
import com.ezz.eshop.adapters.ProductsAdapter
import com.ezz.eshop.databinding.ActivityMainBinding
import com.ezz.eshop.models.Product
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val productsVM by viewModels<ProductsViewModel>()
    private var layoutType = 1
    private lateinit var linearLayout: LinearLayoutManager
    private lateinit var gridLayout: StaggeredGridLayoutManager
    lateinit var adapter: ProductsAdapter
    private var productsList = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        initRecyclerView()
        initViewModel()
        setSupportActionBar(b.topAppBar)
        // initialise top app bar menu buttons
        b.topAppBar.setOnMenuItemClickListener { item ->

            when (item.itemId) {

                R.id.view_button -> {
                    changeView()
                }
                else -> {}
            }
            return@setOnMenuItemClickListener true
        }

        b.cartFab.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    /**
     * initialise product viewModel
     */
    private fun initViewModel() {
        productsVM.productsListLD.observe(this) {
            productsList = it.shuffled()
            adapter.updateList(productsList)
        }

        productsVM.loading.observe(this) {
            b.loading.visibility = if (it) View.VISIBLE else View.GONE
        }

        // show dialog if there is error
        productsVM.errorMessage.observe(this) {
            MaterialAlertDialogBuilder(this).setTitle(it)
                .setPositiveButton("Retry") { _, _ ->
                    productsVM.getAllProducts()
                }.setNegativeButton("Cancel", null)
                .show()
        }

        productsVM.getAllProducts()
    }

    /**
     * changes layout manager for recyclerView
     */
    private fun changeView() {
        layoutType = if (layoutType == 0) 1 else 0
        when (layoutType) {
            0 -> {
                b.productsRecycler.layoutManager = linearLayout
            }
            else -> {
                b.productsRecycler.layoutManager = gridLayout
            }
        }
        adapter.updateLayout(layoutType)
    }

    /**
     * initialise product recyclerView, layout managers and adapter
     */
    private fun initRecyclerView() {
        linearLayout = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )

        gridLayout = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        b.productsRecycler.layoutManager = when (layoutType) {
            0 -> linearLayout
            else -> gridLayout
        }
        adapter = ProductsAdapter(layoutType)
        b.productsRecycler.adapter = adapter
    }

    /**
     * create Search view on creation of options menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.menu_search)
        val searchViewAndroidActionBar = searchViewItem.actionView as SearchView
        searchViewAndroidActionBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewAndroidActionBar.clearFocus()
                val queryList = productsList.filter { it.title!!.lowercase().contains(query) }
                adapter.updateList(queryList)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    adapter.updateList(productsList) // show all items if search empty
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}
