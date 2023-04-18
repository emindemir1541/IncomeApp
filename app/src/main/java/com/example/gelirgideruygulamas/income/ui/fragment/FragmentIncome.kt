package com.example.gelirgideruygulamas.income.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.income.data.room.Income
import com.example.gelirgideruygulamas.income.data.room.IncomeViewModel
import com.example.gelirgideruygulamas.main.data.sharedPreference.PageSettings
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.FragmentIncomeBinding
import com.example.gelirgideruygulamas.databinding.LayoutAddIncomeBinding
import com.example.gelirgideruygulamas.expense.common.util.Message
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.toLong
import com.example.gelirgideruygulamas.income.common.IncomeCardType
import com.example.gelirgideruygulamas.income.ui.component.DialogUtil
import com.example.gelirgideruygulamas.main.common.constant.TaggedCard
import com.example.gelirgideruygulamas.main.data.sharedPreference.SavedMoney
import com.example.gelirgideruygulamas.main.ui.component.CalendarUtil.setMinDate
import com.example.gelirgideruygulamas.main.ui.component.CalendarUtil.setMinDateNow
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentIncome(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton) :
    Fragment() {

    private lateinit var incomeViewModel: IncomeViewModel
    private lateinit var binding: FragmentIncomeBinding
    private lateinit var bindingDialog: LayoutAddIncomeBinding
    private lateinit var adapter: IncomeAdapter
    private var fullScreenDialog: Dialog? = null
    private val savedMoney = SavedMoney(mContext)
    private val statedDate = StatedDate(mContext)
    private val message = Message(mContext)
    private var datePicker: MaterialDatePicker<Long>? = null


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

        setDateTimePicker()

    }

    private fun setRvIncome() {
        binding.rvIncome.setHasFixedSize(true)
        binding.rvIncome.layoutManager = LinearLayoutManager(mContext)

        getData()

        adapter =
            IncomeAdapter(
                mContext,
                mAppCompatActivity,
                this,
                fab_add,
            )
        binding.rvIncome.adapter = adapter

    }

    private fun setDateTimePicker() {
        cardDateToday(statedDate.isToday)
        binding.cardDateButton.setOnClickListener {
            statedDate.setToday()
            cardDateToday(statedDate.isToday)
            getData()
        }
        binding.cardDateButtonSelected.setOnClickListener() {
            setDateTimePickerCard(binding.cardDateButton)
        }
        binding.cardDateRightArrow.setOnClickListener {
            statedDate.addMonth()
            cardDateToday(statedDate.isToday)
            getData()

        }
        binding.cardDateLeftArrow.setOnClickListener {
            statedDate.subtractMonth()
            cardDateToday(statedDate.isToday)
            getData()
        }
    }

    private fun getData() {
        incomeViewModel.readSelectedData.observe(viewLifecycleOwner) { incomeList ->
            adapter.setData(sortListIncome(incomeList))
        }
    }

    private fun setDateTimePickerCard(button: Button) {

        if (datePicker == null) {
            datePicker = MaterialDatePicker.Builder.datePicker().setSelection(statedDate.dateLong).build()
            datePicker!!.show(mAppCompatActivity.supportFragmentManager, "tag")
        }


        datePicker!!.addOnPositiveButtonClickListener { timeInMillis ->
                statedDate.setDate(timeInMillis)
                button.text = statedDate.month
        }

        datePicker!!.addOnCancelListener {
            datePicker = null
        }

    }

    private fun setFabButton() {
        fab_add.setOnClickListener {
            if (PageSettings(mContext).pageLocation == R.id.itemIncome)
                setFullScreenDialogIncome()
        }
    }

    private fun cardDateToday(isToday: Boolean) {
        binding.cardDateButton.isVisible = !isToday
        binding.cardDateButtonSelected.isVisible = isToday
        binding.cardDateButton.text = StatedDate(mContext).month
        binding.cardDateButtonSelected.text = StatedDate(mContext).month
    }


    private fun setFullScreenDialogIncome() {
        if (fullScreenDialog == null) {


            fullScreenDialog = DialogUtil.materialThemeDialog(mContext)

            var isDateSelected = false
            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(StatedDate(mContext).dateLong).setMinDateNow().build()
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
                    val date = DateUtil.convertToDateTime(mTimeInMillis)
                    val income = Income(
                        bindingDialog.layoutIncomeAddIncomeName.text.toString(),
                        bindingDialog.layoutIncomeAddAmount.text.toString().toFloat(),
                        mTimeInMillis,
                        date.dayOfMonth,
                        date.monthValue,
                        date.year,
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

    private fun sortListIncome(incomeList: List<Income>): ArrayList<TaggedCard<Income>> {
        val formattedIncomeList = ArrayList<TaggedCard<Income>>()
        val incomeOnceList = ArrayList<TaggedCard<Income>>()
        val incomeRepeatList = ArrayList<TaggedCard<Income>>()

        for (income in incomeList) {
            if (income.isRepeatable) {
                incomeRepeatList.add(TaggedCard(IncomeCardType.REPEATABLE_CARD, income))
            }
            else {
                incomeOnceList.add(TaggedCard(IncomeCardType.ONCE_CARD, income))
            }
        }
        formattedIncomeList.addAll(incomeRepeatList)
        formattedIncomeList.addAll(incomeOnceList)
        if (!savedMoney.isPermanentEmpty) {
            val date = savedMoney.savedDate
            formattedIncomeList.add(TaggedCard(IncomeCardType.PERMANENT_CARD, Income("Money From Last Month", savedMoney.permanentMoney, savedMoney.savedDate.toLong(), date.dayOfMonth, date.monthValue, date.year, deleted = false, isRepeatable = false))) // TODO: make special card for Permenant money
        }
        formattedIncomeList.add(TaggedCard(IncomeCardType.INVISIBLE_CARD))
        return formattedIncomeList
    }


}