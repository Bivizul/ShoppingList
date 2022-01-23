package com.android.example.shoppinglist.presentation

import android.content.Context
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
import com.android.example.shoppinglist.databinding.FragmentShopItemBinding
import com.android.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment : Fragment() {

    // ссылка на viewModel
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // проверяем все ли параметры передаются
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    // вызывается после onCreatedView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // инициализируем ссылку на viewModel
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // добавляем слушателей ввода текста
        addTextChangeListeners()
        // запускаем правильный режим экрана
        launchRightMode()
        // подписываемся на все объекты viewModel
        observeViewModel()
    }


    // подпишемся на остальные объекты из viewModel
    private fun observeViewModel() {
        // если работа с экраном завершена, то закроем экран
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            // если activity не null, сделает так же как кнопка назад
            // безопасный способ activity?.onBackPressed()
            // activity может вернуть null объект
            // requireActivity возвращеет не null объект
            onEditingFinishedListener.onEditingFinished()
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
        binding.etName.addTextChangedListener(object : TextWatcher {
            // скрываем ошибку при вводе текста
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
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
        binding.saveButton.setOnClickListener {
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        // проверим содержание параметры на содержание ключа, при null кинем исключение
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}
