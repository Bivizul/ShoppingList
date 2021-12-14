package com.android.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.example.shoppinglist.R

// данный экран может работать в режиме добавления и в режиме редактирования
class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        // получим переданные значения
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        // выводим лог, просто mode передать нельзя, так как он NULL, приложение упадет
        Log.d("ShopItemActivity", mode.toString())
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"

        // хорошая практика использовать статические методы указанные ниже

        // запускаем экран в режимае добавления
        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        // запускаем экран в режимае редактирования
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}