package com.example.gelirgideruygulamas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gelirgideruygulamas.calculation.creator.Creator
import com.example.gelirgideruygulamas.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.databinding.ActivityMainBinding
import com.example.gelirgideruygulamas.fragments.expense.FragmentExpense
import com.example.gelirgideruygulamas.fragments.income.FragmentIncome
import com.example.gelirgideruygulamas.fragments.main.FragmentMain

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






    }

    //Fragment Sayfaların Gelir-Ana Sayfa- Gider
    private fun setFragmentIncome() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentIncome(this, this, binding.fabAdd, binding.fabEditModeBack, binding.fabEditModeDelete)).commit()
    }

    private fun setFragmentMain() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentMain(this)).commit()
    }

    private fun setFragmentExpense() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentExpense(this, this, binding.fabAdd, binding.fabEditModeBack, binding.fabEditModeDelete)).commit()
    }

    private fun setNavigationBar() {
        binding.bottomNavigation.setOnItemSelectedListener { selectedItem ->
            PageLocation(this).setValue(selectedItem.itemId)
            when (selectedItem.itemId) {
                R.id.itemIncome -> {
                    setFragmentIncome()
                    editModeFabButtons()
                    true
                }

                R.id.itemHome -> {
                    setFragmentMain()
                    setFabButtonStart()
                    true
                }
                R.id.itemExpense -> {
                    setFragmentExpense()
                    editModeFabButtons()
                    true
                }
                else -> false
            }
        }
    }

    private fun setFabButtonStart() {
        binding.fabAdd.hide()
        binding.fabEditModeBack.hide()
        binding.fabEditModeDelete.hide()
    }

    private fun editModeFabButtons() {
        binding.fabAdd.show()
        binding.fabEditModeBack.hide()
        binding.fabEditModeDelete.hide()
    }


}

// TODO: 8.09.2022 tarih butonunda buayı farklı bir şekilde göster, tarih butonunun üzerine ilk basıldığında buaya gitsin,sonra tarih seçme yeri açılsın