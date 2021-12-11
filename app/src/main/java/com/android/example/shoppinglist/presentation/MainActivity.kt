package com.android.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shoppinglist.R
import com.android.example.shoppinglist.domain.ShopItem

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