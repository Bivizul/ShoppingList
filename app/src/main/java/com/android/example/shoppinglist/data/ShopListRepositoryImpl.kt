package com.android.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.example.shoppinglist.domain.ShopItem
import com.android.example.shoppinglist.domain.ShopListRepository

// сlass realizuyushii interface - Impl
// один и тот е экземпляр, чтобы на всехэкранах работал с одним и тем же репозиторием
object ShopListRepositoryImpl: ShopListRepository {

    private val shopListLD = MutableLiveData<List<ShopItem>>()
    private val shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0
    init{
        for (i in 0 until 10){
            val item = ShopItem("Name $i", i, true)
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        //val oldElement = getShopItem(shopItem.id)
        //shopList.remove(oldElement)
        shopList.remove(getShopItem(shopItem.id))
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        // если NULL это нормально то ставим ShopItem? без выброса throw
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")
        // бросит исключение если не найдет объект
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        // возвращать лучше копию объекта
        return shopListLD
    }

    private fun updateList(){
        shopListLD.value = shopList.toList()    // возвращаем копию листа
    }
}