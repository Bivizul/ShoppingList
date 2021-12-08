package com.android.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.example.shoppinglist.R

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
//        llshopList = findViewById(R.id.ll_shop_list)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]      // 1-variant NEW
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)       // 2-variant OLD
        viewModel.shopList.observe(this) {
/*            Log.d("mainActivityTest", it.toString())    // передаем список елементов приведенный к строчному виду
            if (count == 0) {
                count++
                val item = it[0]
                viewModel.changeEnableState(item)
            }
            showList(it)*/
            shopListAdapter.shopList = it
        }
//        viewModel.getShopList()
    }

    private fun setupRecycleView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
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
    }

    /*private fun showList(list: List<ShopItem>) {
        llshopList.removeAllViews()
        for (shopItem in list){
            val layoutId = if (shopItem.enable){
                R.layout.item_shop_enabled
            } else {
                R.layout.item_shop_disablled
            }
            // из макета в layout создаем view
            // inflate и findViewById очень ресурсозатратные операции, нужно реже выполнять
            val view = LayoutInflater.from(this).inflate(layoutId, llshopList, false)
            val tvName = view.findViewById<TextView>(R.id.tv_name)
            val tvCount = view.findViewById<TextView>(R.id.tv_count)
            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()
            view.setOnLongClickListener{
                viewModel.changeEnableState(shopItem)
                true
            }
            llshopList.addView(view)
        }
    }*/
}