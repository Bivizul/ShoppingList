package com.android.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.example.shoppinglist.R
import com.android.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID
) : Fragment() {

    // ссылка на viewModel
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // уиз макета создаем view
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    // вызывается после onCreatedView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // проверяем все ли параметры передаются
        parseParams()
        // инициализируем ссылку на viewModel
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        // вызовим метод инициализации, все view элементы
        initViews(view)
        // добавляем слушателей ввода текста
        addTextChangeListeners()
        // запускаем правильный режим экрана
        launchRightMode()
        // подписываемся на все объекты viewModel
        observeViewModel()
    }


    // подпишемся на остальные объекты из viewModel
    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = message
        }
        // если работа с экраном завершена, то закроем экран
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            // если activity не null, сделает так же как кнопка назад
            // безопасный способ activity?.onBackPressed()
            // activity может вернуть null объект
            // requireActivity возвращеет не null объект
            activity?.onBackPressed()
            //requireActivity().onBackPressed()
        }
    }

    // запускаем правильный режим экрана
    private fun launchRightMode() {
        // настраиваем экран
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    // устанавливаем слушателя текста
    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            // скрываем ошибку при вводе текста
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        etCount.addTextChangedListener(object : TextWatcher {
            // скрываем ошибку при вводе текста
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun launchEditMode() {
        // получаем элемент по id
        viewModel.getShopItem(shopItemId)
        // подписываемся на данный элемент
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun parseParams() {
        // проверим сожерание intent(намерения), при null кинем исключение
// requireActivity - возвращает ссылку на activity к которой прикреплен данный фрагмент
//        if (!requireActivity().intent.hasExtra(EXTRA_SCREEN_MODE)) {
//            throw RuntimeException("Param screen mode is absent")
//        }
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Param screen mode is absent")
        }
        if (screenMode == MODE_EDIT && shopItemId == ShopItem.UNDEFINED_ID) {
            throw RuntimeException("Param shop item id is absent")
        }
    }

    // инициализация всех VIEW
    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.save_button)
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment{
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment{
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }

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
