package com.android.example.shoppinglist.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shoppinglist.R
import com.android.example.shoppinglist.domain.ShopItem

// RecyclerView создает столько элементов, сколько нужно для вывода на экран + запас снизу и сверху
class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        // создаем view из макета
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_disablled,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        val status = if (shopItem.enable) {
            "Active"
        } else {
            "Not active"
        }
        viewHolder.view.setOnLongClickListener {
            true
        }
        if (shopItem.enable) {
            viewHolder.tvName.text = "${shopItem.name} $status"
            viewHolder.tvCount.text = shopItem.count.toString()
            viewHolder.tvName.setTextColor(
                ContextCompat.getColor(
                    viewHolder.view.context,
                    android.R.color.holo_red_light
                )
            )
        }
    }
    // вместо блока else в onBindViewHolder
    // устанавливает значения по умолчанию
    override fun onViewRecycled(viewHolder: ShopItemViewHolder) {
        super.onViewRecycled(viewHolder)
        viewHolder.tvName.text = ""
        viewHolder.tvCount.text = ""
        viewHolder.tvName.setTextColor(
            ContextCompat.getColor(
                viewHolder.view.context,
                android.R.color.white
            )
        )
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

/*    // возвращает тип view по его позиции
    // нужен когда для разных объектов разные layout
    // сильно нагружает систему
    override fun getItemViewType(position: Int): Int {
        return position
    }*/

    // класс хранящий view
    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }
}