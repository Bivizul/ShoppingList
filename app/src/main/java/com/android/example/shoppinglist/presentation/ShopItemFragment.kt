package com.android.example.shoppinglist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.example.shoppinglist.R

class ShopItemFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // устанавливаем макет
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }
}