package com.android.example.shoppinglist.presentation

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shoppinglist.R
import com.android.example.shoppinglist.databinding.ItemShopDisablledBinding

// класс хранящий view
class ShopItemViewHolder(val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root)
