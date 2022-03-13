package com.ezz.eshop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezz.eshop.R
import com.ezz.eshop.databinding.ReviewProductLayoutBinding
import com.ezz.eshop.models.Product

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    private var datalist = listOf<Product>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.review_product_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.b.product = datalist[position]
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Product>) {
        datalist = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ReviewProductLayoutBinding.bind(itemView)
    }
}
