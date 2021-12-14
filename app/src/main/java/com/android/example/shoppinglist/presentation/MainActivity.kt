package com.android.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // так как точно будет присваивать значение, поэтому можем использовать lateinit var
    // чтобы не делать постоянную проверку на NULL
    private lateinit var viewModel: MainViewModel

    //    private lateinit var llshopList: LinearLayout
    private lateinit var shopListAdapter: ShopListAdapter

//    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecycleView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]      // 1-variant NEW
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)       // 2-variant OLD
        viewModel.shopList.observe(this) {
            // вызываем в новом потоке обновленный список
            shopListAdapter.submitList(it)
        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        buttonAddItem.setOnClickListener {
            // создаем новый экран
            val intent = ShopItemActivity.newIntentAddItem(this)
            // передаем режим работы экрана в качестве параметра
            //intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)   // сам режим mode_add
            // запускаем созданный экран
            startActivity(intent)
        }
    }

    private fun setupRecycleView() {
        // установка RecyclesView
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        // Вставляем адаптер и настраиваем RecycleView
        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    // установка слушателей
    // долгий клик
    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    // короткий клик
    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("MainActivity", it.toString())
            // создаем новый экран
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            // запускаем созданный экран
            startActivity(intent)
        }
    }

    // свайп
    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    // получаем текущий список с которым работает адаптер
                    val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteShopItem(item)
                    Toast.makeText(
                        this@MainActivity,
                        "Удалено",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

}