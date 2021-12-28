package com.android.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.example.shoppinglist.R
import com.android.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

// данный экран может работать в режиме добавления и в режиме редактирования
class ShopItemActivity : AppCompatActivity() {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // устанавливаем контент
        setContentView(R.layout.activity_shop_item)
        // проверяем все ли параметры передаются intent-u
        parseIntent()
        if(savedInstanceState == null) {
            // запускаем фрагмент в нужном режиме
            launchRightMode()
        }
    }
    // запускаем правильный режим экрана
    private fun launchRightMode() {
        val fragment = when (screenMode) {  // если режим подгодящий, то возвращаем экземпляр фрагмента
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        // утанавливаем экземплр фрагмента в данный контейнер
        supportFragmentManager.beginTransaction()   // стартуем транзакцию
            .replace(R.id.shop_item_container, fragment)    // транзакция, удаляет старый фрагмент и добавляет новый
            .commit()   //  запускает транзакцию на выполнение
    }

    private fun parseIntent() {
        // проверим сожерание intent(намерения), при null кинем исключение
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        // хорошая практика использовать статические методы указанные ниже

        // запускаем экран в режимае добавления
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        // запускаем экран в режимае редактирования
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}