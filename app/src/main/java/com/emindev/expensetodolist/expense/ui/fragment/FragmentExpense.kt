package com.emindev.expensetodolist.expense.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.databinding.FragmentExpenseBinding
import com.emindev.expensetodolist.databinding.LayoutAddExpenseBinding
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.expense.common.util.Message
import com.emindev.expensetodolist.main.data.room.Expense
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper.isDarkThemeOn
import com.emindev.expensetodolist.income.ui.component.DialogUtil
import com.emindev.expensetodolist.main.data.sharedPreference.PageSettings
import com.emindev.expensetodolist.main.data.sharedPreference.StatedDate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FragmentExpense(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton) :
    Fragment() {

    private lateinit var binding: FragmentExpenseBinding
    private lateinit var bindingDialog: LayoutAddExpenseBinding
    private var datePicker: MaterialDatePicker<Long>? = null

    private val statedDate = StatedDate(mContext)
    private lateinit var adapter: ExpenseAdapter
    private var fullScreenDialog: Dialog? = null
    private val message = Message(mContext)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExpenseBinding.bind(inflater.inflate(R.layout.fragment_expense, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabButton()
        setRvExpense()
        setDateTimePicker()


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

    private fun setRvExpense() {

        binding.rvExpense.setHasFixedSize(true)
        binding.rvExpense.layoutManager = LinearLayoutManager(mContext)

        getData()

        adapter = ExpenseAdapter(
            mContext,
            mAppCompatActivity,
            this,
            fab_add
        )
        binding.rvExpense.adapter = adapter
    }

    private fun getData() {
    }

    private fun setFabButton() {
        // fab_add.animate(R.layout.layout_add_expense)
        fab_add.setOnClickListener {
            if (PageSettings(mContext).pageLocation == R.id.itemExpense)
                fab_add.hide()
            setFullScreenDialogExpense()

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

    private fun cardDateToday(isToday: Boolean) {
        binding.cardDateButton.isVisible = !isToday
        binding.cardDateButtonSelected.isVisible = isToday
        binding.cardDateButton.text = StatedDate(mContext).month
        binding.cardDateButtonSelected.text = StatedDate(mContext).month
        if (mContext.isDarkThemeOn)
        binding.cardDateButtonSelected.setTextColor(Color.WHITE)
    }

    private fun setFullScreenDialogExpense() {
        if (fullScreenDialog == null) {


            fullScreenDialog = DialogUtil.materialThemeDialog(mContext)

            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(StatedDate(mContext).dateLong).build()
            var mTimeInMillis: Long = StatedDate(mContext).dateLong

            bindingDialog = LayoutAddExpenseBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScale)
            fullScreenDialog?.show()


            bindingDialog.layoutExpenseAddRepetationType2.isChecked = true
            bindingDialog.layoutExpenseAddLenderView.isVisible = false
            bindingDialog.layoutExpenseAddMonthlyView.isVisible = false
            bindingDialog.layoutExpenseAddDelete.isVisible = false
            bindingDialog.layoutExpenseAddDone.isVisible = false
            bindingDialog.layoutExpenseAddRepetitionView.isVisible = true
            bindingDialog.layoutExpenseAddTypeWant.isChecked = true
            bindingDialog.layoutExpenseAddLenderView.isVisible = false
            bindingDialog.layoutExpenseAddDate.text = mContext.getString(R.string.expense_day)
            if(mContext.isDarkThemeOn)
                bindingDialog.layoutExpenseAddDate.setTextColor(Color.WHITE)


            fun emptySafe(): Boolean {
                if (bindingDialog.layoutExpenseAddRepetationType1.isChecked && bindingDialog.layoutExpenseAddDate.text.toString().isEmpty()) return false
                if (bindingDialog.layoutExpenseAddTypeDebt.isChecked && bindingDialog.layoutExpenseAddLender.text.toString().isEmpty()) return false
                if (bindingDialog.layoutExpenseAddRepetationType1.isChecked && bindingDialog.layoutExpenseAddRepetation.text.toString().isEmpty()) return false
                return bindingDialog.layoutExpenseAddAmount.text.toString().isNotEmpty() &&
                        bindingDialog.layoutExpenseAddExpenseName.text.toString().isNotEmpty()
            }

            fun setDateTimePickerDialog() {
                if (!datePicker.isVisible) {

                    datePicker.show(mAppCompatActivity.supportFragmentManager, "tag")

                    datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                        bindingDialog.layoutExpenseAddDate.text = DateUtil.convertToString(timeInMillis)
                        mTimeInMillis = timeInMillis
                    }
                }
            }

            bindingDialog.layoutExpenseAddRepetation.addTextChangedListener {
                val repetition = bindingDialog.layoutExpenseAddRepetation.text
                if (repetition!!.isNotEmpty() && repetition.toString().toInt() > 24) {
                    repetition.clear()
                    message.warningMuchCharacter(24)
                }
                if (repetition.isNotEmpty() && repetition.toString().toInt() < 1) {
                    repetition.clear()
                    message.lessCharacterWarning(1)
                }
            }


            bindingDialog.layoutExpenseAddExpenseType.setOnCheckedChangeListener { _, _ ->
                when {
                    bindingDialog.layoutExpenseAddTypeNeed.isChecked -> {
                        bindingDialog.layoutExpenseAddLenderView.isVisible = false
                    }

                    bindingDialog.layoutExpenseAddTypeWant.isChecked -> {
                        bindingDialog.layoutExpenseAddLenderView.isVisible = false
                    }

                    bindingDialog.layoutExpenseAddTypeDebt.isChecked -> {
                        bindingDialog.layoutExpenseAddLenderView.isVisible = true
                    }
                }
            }

            bindingDialog.layoutExpenseAddRepetationType.setOnCheckedChangeListener { _, _ ->
                if (bindingDialog.layoutExpenseAddRepetationType1.isChecked) {
                    bindingDialog.layoutExpenseAddMonthlyView.isVisible = true
                }
                if (bindingDialog.layoutExpenseAddRepetationType2.isChecked) {
                    bindingDialog.layoutExpenseAddMonthlyView.isVisible = false
                }
            }

            fullScreenDialog?.setOnCancelListener {
                fab_add.show()
                fullScreenDialog = null
            }

            bindingDialog.layoutExpenseAddClose.setOnClickListener {
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }

            bindingDialog.layoutExpenseAddSave.setOnClickListener {
                if (emptySafe()) {
                    val date = DateUtil.convertToDateTime(mTimeInMillis)

                    fullScreenDialog?.cancel()
                    fullScreenDialog = null
                    getData()
                }
                else {
                    message.warningEmpty
                }
            }

            bindingDialog.layoutExpenseAddDate.setOnClickListener {
                setDateTimePickerDialog()
            }

        }
    }


}
