package com.android.example.shoppinglist.domain

data class ShopItem(
    // сначала указываем обязательные поля, затем необязательные поля
    val name: String,
    val count: Int,
    val enable: Boolean,
    var id: Int = UNDEFINED_ID
    // любые жестко закодированные числа лучше выводить в КОНСТАНТУ
){
    companion object{
        const val UNDEFINED_ID = 0
    }
}


