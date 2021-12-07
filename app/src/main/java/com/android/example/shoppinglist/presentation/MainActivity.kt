package com.android.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.android.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

    // так как точно будет присваивать значение, поэтому можем использовать lateinit var
    // чтобы не делать постоянную проверку на NULL
    private lateinit var viewModel: MainViewModel

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]      // 1-variant NEW
        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)       // 2-variant OLD
        viewModel.shopList.observe(this){
            Log.d("mainActivityTest", it.toString())    // передаем список елементов приведенный к строчному виду
            if (count == 0) {
                count++
                val item = it[0]
                viewModel.changeEnableState(item)
            }
        }
//        viewModel.getShopList()
    }
}