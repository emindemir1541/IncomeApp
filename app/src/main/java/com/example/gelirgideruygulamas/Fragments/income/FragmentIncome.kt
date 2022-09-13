package com.example.gelirgideruygulamas.fragments.income

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.data.income.Income
import com.example.gelirgideruygulamas.data.income.IncomeViewModel
import com.example.gelirgideruygulamas.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.FragmentIncomeBinding
import com.example.gelirgideruygulamas.databinding.LayoutAddIncomeBinding
import com.example.gelirgideruygulamas.feedback.Message
import com.example.gelirgideruygulamas.helper.DateHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentIncome(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    Fragment() {

    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var binding: FragmentIncomeBinding
    private lateinit var bindingDialog: LayoutAddIncomeBinding
    private lateinit var adapter: IncomeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIncomeBinding.bind(inflater.inflate(R.layout.fragment_income, container, false))

        incomeViewModel = ViewModelProvider(this)[IncomeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRvIncome()

        scrollSettings()

        setFabButton()

    }

    private fun setRvIncome() {
        binding.rvIncome.setHasFixedSize(true)
        binding.rvIncome.layoutManager = LinearLayoutManager(mContext)

        incomeViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            val incomeNewList = incomeViewModel.readSelectedData(mContext)
            adapter.setData(incomeNewList)
        })

        adapter =
            IncomeAdapter(
                mContext,
                mAppCompatActivity,
                fab_add,
                fab_editMode_back,
                fab_editMode_delete
            )
        binding.rvIncome.adapter = adapter

    }

    private fun setFabButton() {
        fab_add.setOnClickListener {
            if (PageLocation(mContext).getValue() == R.id.itemIncome)
                setAlertDialogIncome()
        }
    }

    private fun setAlertDialogIncome() {

        bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(context))

        val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
        alertDialogBuilder.setView(bindingDialog.root)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()


        bindingDialog.incomeAddPageIncomeDate.addTextChangedListener {
            val incomeDate = bindingDialog.incomeAddPageIncomeDate
            if (incomeDate.text!!.isNotEmpty() && incomeDate.text.toString().toInt() > 31) {
                incomeDate.text!!.clear()
                Message(mContext).muchCharacterWarning(31)
            }
        }

        bindingDialog.incomeAddPageAddButton.setOnClickListener {

            fun emptySafe(): Boolean {
                return bindingDialog.incomeAddPageIncomeName.text.toString().isNotEmpty() && bindingDialog.incomeAddPageIncomeDate.text.toString().isNotEmpty() && bindingDialog.incomeAddPageIncomeAmount.text.toString().isNotEmpty()
            }

            if (emptySafe()) {
                val statedDateTime = StatedDate(mContext).getDateTime()
                incomeViewModel.addIncome(
                    Income
                        (
                        bindingDialog.incomeAddPageIncomeName.text.toString(),
                        bindingDialog.incomeAddPageIncomeAmount.text.toString().toFloat(),
                        StatedDate(mContext).getDateLong(),
                        DateHelper.toLong(statedDateTime.year, statedDateTime.monthValue, bindingDialog.incomeAddPageIncomeDate.text.toString().toInt(), statedDateTime.hour, statedDateTime.minute, statedDateTime.second, statedDateTime.nano),
                        false
                    )
                )
                alertDialog.dismiss()
            }
            else {
                Message(mContext).emptyWarning()
            }

        }

    }

    private fun scrollSettings() {

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