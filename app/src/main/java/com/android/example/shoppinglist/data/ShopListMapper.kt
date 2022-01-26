package com.android.example.shoppinglist.data

import com.android.example.shoppinglist.domain.ShopItem

// класс преобразующий один объект в другой называется Mapper
class ShopListMapper {

    // преобразуем сущность Domain слоя в сущность Data слоя
    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        name = shopItem.name,
        count = shopItem.count,
        enable = shopItem.enable
    )

    // преобразуем сущность Data слоя в сущность Domain слоя
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        name = shopItemDbModel.name,
        count = shopItemDbModel.count,
        enable = shopItemDbModel.enable
    )

    // преобразуем List объектов ShopItemDbModel в List объектов ShopItem
    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map{
        mapDbModelToEntity(it)
    }
}