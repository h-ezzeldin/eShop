package com.ezz.eshop.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezz.eshop.R
import com.ezz.eshop.databinding.ProductGridLayoutBinding
import com.ezz.eshop.databinding.ProductLinearLayoutBinding
import com.ezz.eshop.models.Product
import com.ezz.eshop.ui.DetailsActivity
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductsAdapter(private var layoutType: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var productList: List<Product> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder = if (viewType == 0) {
            LinearViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_linear_layout, parent, false)
            )
        } else {
            GridViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_grid_layout, parent, false)
            )
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return layoutType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var imageView: ImageView?
        var toCart: MaterialButton?

        if (layoutType == 0) {
            val myHolder = (holder as LinearViewHolder)
            myHolder.b.linearProduct = productList[position]
            imageView = myHolder.b.productImage
            toCart = myHolder.b.toCartButton
        } else {
            val myHolder = (holder as GridViewHolder)
            myHolder.b.gridProduct = productList[position]
            imageView = myHolder.b.productImage
            toCart = myHolder.b.toCartButton
        }

        Glide
            .with(holder.itemView.context)
            .load(productList[position].image)
            .override(300, 300)
            .into(imageView)

        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, DetailsActivity::class.java).apply {
                putExtra("id", productList[position].id)
            }

            val actOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                (holder.itemView.context as Activity),
                imageView,
                "image_trans"
            )

            (holder.itemView.context as Activity).startActivity(intent, actOptions.toBundle())
        }

        toCart.setOnClickListener {
            productList[position].id?.let { _ ->
                addToCart(
                    holder.itemView.context,
                    productList[position]
                )
            }
        }
    }

    private fun addToCart(context: Context, product: Product) {
        product.quantity = 1
        var cart = mutableListOf<Product>()
        val sharedPrefs = context.getSharedPreferences("app", Context.MODE_PRIVATE)
        val cartString = sharedPrefs.getString("cart", "")
        if (cartString != "") {
            cart = fromJsonTOList(cartString!!).toMutableList()
            Log.d("TAG", "addToCart: $cart")
        }
        val itemInCart = cart.find { it.id == product.id }
        if (itemInCart == null) {
            cart.add(product)
        } else {
            itemInCart.quantity = itemInCart.quantity?.plus(1)
        }

        Log.d("TAG", "addToCart: $cart")
        sharedPrefs.edit().putString("cart", Gson().toJson(cart)).apply()
        Toast.makeText(context, "Item Added!", Toast.LENGTH_SHORT).show()
    }

    private fun fromJsonTOList(json: String): List<Product> {
        val itemType = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(json, itemType)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Product>) {

        productList = list
        Log.d("TAG", "updateList: ${productList.size}")
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLayout(type: Int) {
        layoutType = type
        notifyDataSetChanged()
    }

    class LinearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ProductLinearLayoutBinding.bind(itemView)
    }

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ProductGridLayoutBinding.bind(itemView)
    }
}
