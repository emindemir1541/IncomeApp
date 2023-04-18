package com.example.gelirgideruygulamas.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.main.data.sharedPreference.PageSettings
import com.example.gelirgideruygulamas.databinding.ActivityMainBinding
import com.example.gelirgideruygulamas.expense.ui.fragment.FragmentExpense
import com.example.gelirgideruygulamas.income.ui.fragment.FragmentIncome
import com.example.gelirgideruygulamas.main.data.viewmodel.MainViewModel
import com.example.gelirgideruygulamas.main.ui.fragment.FragmentMain


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationBar()
        binding.bottomNavigation.selectedItemId = R.id.itemHome
        setFragmentMain()
        mainViewModel._isLoading.value = false





    }


    private fun setFragmentIncome() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentIncome(this, this, binding.fabAdd)).commit()

    }

    private fun setFragmentMain() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentMain(this)).commit()
    }

    private fun setFragmentExpense() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentExpense(this, this, binding.fabAdd)).commit()
    }

    private fun setNavigationBar() {
        binding.bottomNavigation.setOnItemSelectedListener { selectedItem ->
            PageSettings(this).setPageLocation(selectedItem.itemId)
            when (selectedItem.itemId) {
                R.id.itemIncome -> {
                    setFragmentIncome()
                    binding.fabAdd.show()
                    true
                }

                R.id.itemHome -> {
                    setFragmentMain()
                    binding.fabAdd.hide()
                    true
                }

                R.id.itemExpense -> {
                    setFragmentExpense()
                    binding.fabAdd.show()
                    true
                }

                else -> false
            }
        }
    }


}
