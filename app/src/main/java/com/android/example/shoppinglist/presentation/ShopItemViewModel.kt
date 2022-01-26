package com.android.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.example.shoppinglist.data.ShopListRepositoryImpl
import com.android.example.shoppinglist.domain.AddShopItemUseCase
import com.android.example.shoppinglist.domain.EditShopItemUseCase
import com.android.example.shoppinglist.domain.GetShopItemUseCase
import com.android.example.shoppinglist.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    // для проверки на пустоту
    // версия для viewModel
    private val _errorInputName = MutableLiveData<Boolean>()

    // версия для activity
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    // для проверки на количество
    // версия для viewModel
    private val _errorInputCount = MutableLiveData<Boolean>()

    // версия для activity
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    // скажем когда можно закрывать экран с типом данных без полезного значения
    // версия для viewModel
    private val _shouldCloseScreen = MutableLiveData<Unit>()
    // версия для activity
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    // загружаем элемент по его ID
    fun getShopItem(shopItemId: Int) {
        // получаем елемент
        val item = getShopItemUseCase.getShopItem(shopItemId)
        // установим элемент в LiveData
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        // получаем имя
        val name = parseName(inputName)
        // получаме количество
        val count = parseCount(inputCount)
        // проверяем корректность введенных полей
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            // получаем объект из LiveData, если он там есть и он не NULL, то выполняем код в {}
            shopItem.value?.let {
                // создаем новый объект путем копированием существуещего
                val item = it.copy(name = name, count = count)  // именнованные параметры
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
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
    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        // проверем пустое ли имя
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        // проверяем на количество
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    // завершение работы
    private fun finishWork(){
        _shouldCloseScreen.value = Unit
    }

}