package com.ezz.eshop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ezz.eshop.databinding.ActivityInfoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoActivity : AppCompatActivity() {
    private lateinit var b: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(b.root)

        // show dialog on click
        b.submit.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Order Submitted!")
                .setPositiveButton("Continue Shopping") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}
