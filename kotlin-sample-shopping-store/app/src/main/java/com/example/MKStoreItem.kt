package com.example

import android.widget.Button
import java.io.Serializable


data class MKStoreItem(
        val name: String,
        val description: String,
        val id: Int,
        val price: Float,
        var count: Int,
        var isSelected: Boolean,
        var button: Button? = null
): Serializable