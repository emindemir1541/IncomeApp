package com.example.gelirgideruygulamas.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gelirgideruygulamas.Adapter.RVincomeAdapter
import com.example.gelirgideruygulamas.DataBases.MySharedPreferences
import com.example.gelirgideruygulamas.DataClass.Income
import com.example.gelirgideruygulamas.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_income.*

class FragmentIncome(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    Fragment() {

    private lateinit var incomeList: ArrayList<Income>
    private lateinit var adapter: RVincomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_income, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SetRVIncome()

        ScrollSettings()

        SetFABButton()

    }

    fun SetRVIncome() {
        rv_income.setHasFixedSize(true)
        rv_income.layoutManager = LinearLayoutManager(mContext)

        incomeList = ArrayList<Income>()

        incomeList.add(Income(2, "gelir2", 545f, "16/25/2215"))
        incomeList.add(Income(3, "gelir3", 684865f, "16/25/2215"))
        incomeList.add(Income(4, "gelir4", 254f, "16/25/2215"))
        incomeList.add(Income(5, "gelir5", 5486f, "16/25/2215"))


        adapter =
            RVincomeAdapter(
                mContext,
                mAppCompatActivity,
                incomeList,
                fab_add,
                fab_editMode_back,
                fab_editMode_delete
            )
        rv_income.adapter = adapter

        /*fab_add.setOnClickListener(){
            IncomeList.add(3,Income(1, "gelir1", 215f, "16/25/2215"))
            IncomeList.removeAt(3)
            adapter.notifyDataSetChanged()
            // TODO: 21.03.2022 eleman ekle ve çıkar
        }*/
    }

    fun SetFABButton() {
        fab_add.setOnClickListener {
            if (MySharedPreferences(mContext).getBottomNavigationLocation() == R.id.itemIncome)
                SetAlertDialogIncome()
        }
    }

    fun SetAlertDialogIncome() {

        val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
        alertDialogBuilder.setView(R.layout.layout_add_income)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        val incomeAddPage_incomeName =
            alertDialog.findViewById<TextInputEditText>(R.id.incomeAddPage_incomeName)
        val incomeAddPage_incomeDate =
            alertDialog.findViewById<TextInputEditText>(R.id.incomeAddPage_incomeDate)
        val incomeAddPage_incomeAmount =
            alertDialog.findViewById<TextInputEditText>(R.id.incomeAddPage_incomeAmount)
        val addButton = alertDialog.findViewById<MaterialButton>(R.id.incomeAddPage_addButton)

        addButton?.setOnClickListener() {
            alertDialog.dismiss()
            // TODO: 20.03.2022  set edittext dialog

        }


    }

    fun ScrollSettings() {

        // TODO: 17.03.2022 HATA

        /*    rv_income.viewTreeObserver.addOnScrollChangedListener() {

                Log.e("aa", rv_income.scrollX.toString())

                Log.e("X", rv_income.scrollY.toString())

                Log.e("Y", rv_income.scaleY.toString())


                if (rv_income.scrollY == 0) {
                    fab_add.show()
                } else {
                    fab_add.hide()
                }
            }*/


    }

}