package com.example.gelirgideruygulamas.income.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.income.data.room.Income
import com.example.gelirgideruygulamas.income.data.room.IncomeViewModel
import com.example.gelirgideruygulamas.main.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.FragmentIncomeBinding
import com.example.gelirgideruygulamas.databinding.LayoutAddIncomeBinding
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentIncome(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton) :
    Fragment() {

    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var binding: FragmentIncomeBinding
    private lateinit var bindingDialog: LayoutAddIncomeBinding
    private var incomeRepeatList = ArrayList<Income>()
    private var incomeOnceList = ArrayList<Income>()
    private var incomeListAll = emptyList<Income>()
    private lateinit var adapter: IncomeAdapter
    private var fullScreenDialog: Dialog? = null

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

        incomeViewModel.readAllData.observe(this.viewLifecycleOwner, Observer {
            incomeListAll = incomeViewModel.readSelectedData(mContext)
        })



    }

    private fun setRvIncome() {
        binding.rvIncome.setHasFixedSize(true)
        binding.rvIncome.layoutManager = LinearLayoutManager(mContext)

        incomeViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            extractList(incomeViewModel.readSelectedData(mContext))
            adapter.setData(incomeOnceList, incomeRepeatList)


        })

        adapter =
            IncomeAdapter(
                mContext,
                mAppCompatActivity,
                this,
                fab_add,
            )
        binding.rvIncome.adapter = adapter

    }

    private fun setFabButton() {
        fab_add.setOnClickListener {
            if (PageLocation(mContext).getValue() == R.id.itemIncome)
                setFullScreenDialogIncome()
        }
    }

    /* private fun setAlertDialogIncome() {



         bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(context))

         val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
         alertDialogBuilder.setView(bindingDialog.root)
         val alertDialog = alertDialogBuilder.create()
         alertDialog.show()


         bindingDialog.incomeAddPageIncomeDate.addTextChangedListener {
             val incomeDate = bindingDialog.incomeAddPageIncomeDate
             if (incomeDate.text!!.isNotEmpty() && incomeDate.text.toString().toInt() > 31) {
                 incomeDate.text!!.clear()
                 Message(mContext).warningMuchCharacter(31)
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
                 Message(mContext).warningEmpty()
             }

         }

     }*/

    private fun setFullScreenDialogIncome() {
        if (fullScreenDialog == null) {


            fullScreenDialog = if (Helper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }

            var isDateSelected = false
            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(StatedDate(mContext).dateLong).build()
            var mTimeInMillis: Long = StatedDate(mContext).dateLong

            bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScale)
            fullScreenDialog?.show()


            //başlangıçta yapılanlar
            bindingDialog.layoutIncomeAddRepetationType2.isChecked = true
            bindingDialog.layoutIncomeAddMonthlyView.isVisible = false
            bindingDialog.layoutIncomeAddDelete.isVisible = false
            bindingDialog.layoutIncomeAddDate.text = mContext.getString(R.string.income_day)


            fun emptySafe(): Boolean {
                if (bindingDialog.layoutIncomeAddRepetationType1.isChecked && !isDateSelected) return false
                return bindingDialog.layoutIncomeAddAmount.text.toString().isNotEmpty() &&
                        bindingDialog.layoutIncomeAddIncomeName.text.toString().isNotEmpty()
            }

            fun setDateTimePickerDialog() {
                if (!datePicker.isVisible) {

                    datePicker.show(mAppCompatActivity.supportFragmentManager, "tag")

                    datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                        bindingDialog.layoutIncomeAddDate.text = DateUtil.convertToString(timeInMillis)
                        mTimeInMillis = timeInMillis
                        isDateSelected = true
                    }
                }
                // TODO: 7.08.2022 datetime pickerde geçmiş aylar seçilmesin
            }


            bindingDialog.layoutIncomeAddRepetationType.setOnCheckedChangeListener { _, _ ->
                if (bindingDialog.layoutIncomeAddRepetationType1.isChecked) {
                    bindingDialog.layoutIncomeAddMonthlyView.isVisible = true
                }
                if (bindingDialog.layoutIncomeAddRepetationType2.isChecked) {
                    bindingDialog.layoutIncomeAddMonthlyView.isVisible = false
                }
            }

            fullScreenDialog?.setOnCancelListener {
                fab_add.show()
                fullScreenDialog = null
            }

            bindingDialog.layoutIncomeAddClose.setOnClickListener {
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }

            bindingDialog.layoutIncomeAddSave.setOnClickListener {

                if (emptySafe()) {

                    val income = Income(
                        bindingDialog.layoutIncomeAddIncomeName.text.toString(),
                        bindingDialog.layoutIncomeAddAmount.text.toString().toFloat(),
                        mTimeInMillis,
                        mTimeInMillis,
                        false,
                        bindingDialog.layoutIncomeAddRepetationType1.isChecked,
                    )
                    incomeViewModel.add(income)
                    fullScreenDialog?.cancel()
                    fullScreenDialog = null
                }
                else {
                    getString(R.string.warning_empty)
                }
            }

            bindingDialog.layoutIncomeAddDate.setOnClickListener {
                setDateTimePickerDialog()
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

    private fun extractList(incomeList: List<Income>) {
        incomeOnceList.clear()
        incomeRepeatList.clear()

        for (income in incomeList) {
            if (income.repetation) {
                incomeRepeatList.add(income)
            }
            else {
                incomeOnceList.add(income)
            }
        }
    }


}