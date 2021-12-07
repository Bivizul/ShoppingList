package com.android.example.shoppinglist.domain

import androidx.lifecycle.LiveData

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopList():LiveData<List<ShopItem>>{
        // чтобы приложение не крашилось на начальном этапе, пишем: T O D O ( )
        return shopListRepository.getShopList()
    }
}