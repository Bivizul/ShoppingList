package com.android.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.*
import com.android.example.shoppinglist.data.ShopListRepositoryImpl
import com.android.example.shoppinglist.domain.DeleteShopItemUseCase
import com.android.example.shoppinglist.domain.EditShopItemUseCase
import com.android.example.shoppinglist.domain.GetShopListUseCase
import com.android.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

// : AndroidViewModel() наследование нужно,если во ViewModel есть контекст
// Если контекста нет то можно наследоваться от : ViewModel , в его конструктор ничего не нужно передавать
// presentation и data слой недолжны знать друг о друге НИЧЕГО
class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem){
        viewModelScope.launch {
            val newItem = shopItem.copy(enable = !shopItem.enable)
            editShopItemUseCase.editShopItem(newItem)
        }
    }
}