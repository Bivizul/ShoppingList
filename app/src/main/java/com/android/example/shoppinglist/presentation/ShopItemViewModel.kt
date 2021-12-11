package com.android.example.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.android.example.shoppinglist.data.ShopListRepositoryImpl
import com.android.example.shoppinglist.domain.AddShopItemUseCase
import com.android.example.shoppinglist.domain.EditShopItemUseCase
import com.android.example.shoppinglist.domain.GetShopItemUseCase
import com.android.example.shoppinglist.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if(fieldsValid){
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if(fieldsValid){
            val shopItem = ShopItem(name, count, true)
            editShopItemUseCase.editShopItem(shopItem)
        }
    }

    private fun parseName(inputName: String?): String {
        // обрезаем пробелы либо выведем пустую строку
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            // обрезаем пробелы и переводим в число, либо вернем ноль
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    // проверяем корректность введенных данных
    private fun validateInput(name: String, count: Int) : Boolean{
        var result = true
        // проверем пустое ли имя
        if(name.isBlank()){
            // TODO: show error input name
            result = false
        }
        // проверяем на количество
        if(count <= 0){
            // TODO: show error input count
            result = false
        }
        return result
    }

}