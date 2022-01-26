package com.android.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.shoppinglist.data.ShopListRepositoryImpl
import com.android.example.shoppinglist.domain.DeleteShopItemUseCase
import com.android.example.shoppinglist.domain.EditShopItemUseCase
import com.android.example.shoppinglist.domain.GetShopListUseCase
import com.android.example.shoppinglist.domain.ShopItem

// : AndroidViewModel() наследование нужно,если во ViewModel есть контекст
// Если контекста нет то можно наследоваться от : ViewModel , в его конструктор ничего не нужно передавать
// presentation и data слой недолжны знать друг о друге НИЧЕГО
class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    // MutableLiveData - это LiveData в которую мы сами можем вставлять объекты
//    val shopList = MutableLiveData<List<ShopItem>>()
    val shopList = getShopListUseCase.getShopList()

//    fun getShopList(){
//        val list = getShopListUseCase.getShopList()
//        // добавляем елемент
//        shopList.value = list      // можно вызывать только из главного потока
//        //shopList.postValue = list       // можно вызывать из любого потока
//    }

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
//        getShopList()
    }

    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enable = !shopItem.enable)
        editShopItemUseCase.editShopItem(newItem)
//        getShopList()

    }
}