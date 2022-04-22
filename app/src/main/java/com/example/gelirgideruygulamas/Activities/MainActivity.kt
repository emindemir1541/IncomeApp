package com.example.gelirgideruygulamas.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gelirgideruygulamas.DataBases.MySharedPreferences
import com.example.gelirgideruygulamas.Fragments.FragmentExpense
import com.example.gelirgideruygulamas.Fragments.FragmentIncome
import com.example.gelirgideruygulamas.Fragments.FragmentMain
import com.example.gelirgideruygulamas.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StartingSettings()

    }


    fun StartingSettings() {
        SetNavigationBar()
        //navigation bar hangi item ile başladı
        bottom_navigation.selectedItemId = R.id.itemHome
        SetFragmentMain()

    }


    //Fragment Sayfaların Gelir-Ana Sayfa- Gider
    fun SetFragmentIncome() {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        ft.add(
            R.id.frame_layout,
            FragmentIncome(this, this, fab_add, fab_editMode_back, fab_editMode_delete)
        )
        ft.commit()
    }

    fun SetFragmentMain() {

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        ft.add(R.id.frame_layout, FragmentMain(this))

        ft.commit()
    }

    fun SetFragmentExpense() {

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        ft.add(
            R.id.frame_layout,
            FragmentExpense(this, this, fab_add, fab_editMode_back, fab_editMode_delete)
        )

        ft.commit()
    }


    fun SetNavigationBar() {
        bottom_navigation.setOnItemSelectedListener { selectedItem ->
            MySharedPreferences(this).saveBottomNavigationLocation(selectedItem.itemId)
            when (selectedItem.itemId) {
                R.id.itemIncome -> {
                    // TODO: 13.03.2022 item tıklandığında yapılacaklar
                    SetFragmentIncome()
                    EditModeFABbuttons(false)
                    true
                }

                R.id.itemHome -> {
                    // TODO: 13.03.2022 item2 tıklandığında yapılacaklar
                    SetFragmentMain()
                    SetFABbuttonStart()
                    true
                }
                R.id.itemExpense -> {
                    // TODO: 13.03.2022 item tıklandığında yapılacaklar
                    SetFragmentExpense()
                    EditModeFABbuttons(false)
                    true
                }
                else -> false
            }
        }
    }

    fun SetFABbuttonStart() {
        fab_add.hide()
        fab_editMode_back.hide()
        fab_editMode_delete.hide()
    }

    fun EditModeFABbuttons(open: Boolean) {
        if (open) {
            fab_add.hide()
            fab_editMode_back.show()
            fab_editMode_delete.show()
        }
        else {
            fab_add.show()
            fab_editMode_back.hide()
            fab_editMode_delete.hide()
        }
    }


}