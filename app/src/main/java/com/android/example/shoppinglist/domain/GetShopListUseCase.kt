package com.android.example.shoppinglist.domain

class GetShopListUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopList(): List<ShopItem>{
        // чтобы приложение не крашилось на начальном этапе, пишем: T O D O ( )
        return shopListRepository.getShopList()
    }
}