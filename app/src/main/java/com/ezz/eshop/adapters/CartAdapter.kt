package com.ezz.eshop.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezz.eshop.R
import com.ezz.eshop.databinding.CartProductLayoutBinding
import com.ezz.eshop.models.Product
import com.google.gson.Gson

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var dataList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_product_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.b.product = dataList[position]
        Glide
            .with(holder.itemView.context)
            .load(dataList[position].image)
            .override(100, 100)
            .into(holder.b.productImage)

        holder.b.minus.setOnClickListener {
            var count = holder.b.count.text.toString().toInt()
            count = if (count > 1) count - 1 else count
            dataList[position].quantity = count
            notifyItemChanged(position)
            updateCart(holder.itemView.context)
        }

        holder.b.plus.setOnClickListener {
            var count = holder.b.count.text.toString().toInt()
            count += 1
            dataList[position].quantity = count
            notifyItemChanged(position)
            updateCart(holder.itemView.context)
        }

        holder.b.deleteButton.setOnClickListener {
            dataList.removeAt(position)
            notifyItemRemoved(position)
            updateCart(holder.itemView.context)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun updateCart(context: Context) {
        val sharedPrefs = context.getSharedPreferences("app", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString("cart", Gson().toJson(dataList)).apply()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Product>) {
        this.dataList = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = CartProductLayoutBinding.bind(itemView)
    }
}
