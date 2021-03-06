package com.android.example.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shoppinglist.R
import com.android.example.shoppinglist.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecycleView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]      // 1-variant NEW
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)       // 2-variant OLD
        viewModel.shopList.observe(this) {
            // вызываем в новом потоке обновленный список
            shopListAdapter.submitList(it)
        }
        binding.buttonAddShopItem.setOnClickListener {
            if(isOnePaneMode()){
                // создаем новый экран
                val intent = ShopItemActivity.newIntentAddItem(this)
                // запускаем созданный экран
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    override fun onEditingFinished(){
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode() : Boolean{
        return binding.shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment){
        // удалит из backstack один фрагмент, а если ничего нет то и делать ничего не будет
        supportFragmentManager.popBackStack()
        // добавляем новый елемент
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment) // заменяем фрагмент в контейнере
            .addToBackStack(null)   // добавляем в backstack
            .commit()
    }

    private fun setupRecycleView() {
        with(binding.rvShopList) {
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
        setupSwipeListener(binding.rvShopList)
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
            if(isOnePaneMode()){
                // создаем новый экран
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                // запускаем созданный экран
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
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