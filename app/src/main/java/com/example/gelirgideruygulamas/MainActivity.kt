package com.example.gelirgideruygulamas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gelirgideruygulamas.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.ActivityMainBinding
import com.example.gelirgideruygulamas.common.feedback.error.Error
import com.example.gelirgideruygulamas.fragments.expense.FragmentExpense
import com.example.gelirgideruygulamas.fragments.income.FragmentIncome
import com.example.gelirgideruygulamas.fragments.main.FragmentMain
import com.sddk.mobileapphelper_main.common.helper.FeedbackUtil.startupProcess


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







        Error(this).sendToServer()

        StatedDate(this).setToday()

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
