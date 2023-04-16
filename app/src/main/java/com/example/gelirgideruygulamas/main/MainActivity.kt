package com.example.gelirgideruygulamas.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.main.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.databinding.ActivityMainBinding
import com.example.gelirgideruygulamas.expense.ui.fragment.FragmentExpense
import com.example.gelirgideruygulamas.income.ui.fragment.FragmentIncome
import com.example.gelirgideruygulamas.main.ui.fragment.FragmentMain


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationBar()
        //navigation bar hangi item ile başladı
        binding.bottomNavigation.selectedItemId = R.id.itemHome
        setFragmentMain()



      //  Error().globalErrorCatcher(this)







       // StatedDate(this).setToday()

    }



    //Fragment Sayfaların Gelir-Ana Sayfa- Gider
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
            PageLocation(this).setValue(selectedItem.itemId)
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
