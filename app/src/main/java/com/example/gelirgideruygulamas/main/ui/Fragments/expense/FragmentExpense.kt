package com.example.gelirgideruygulamas.main.Fragments.expense

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.main.data.expense.Expense
import com.example.gelirgideruygulamas.main.data.expense.ExpenseViewModel
import com.example.gelirgideruygulamas.main.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.FragmentExpenseBinding
import com.example.gelirgideruygulamas.databinding.LayoutAddExpenseBinding
import com.example.gelirgideruygulamas.fragments.expense.ExpenseAdapter
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper
import com.example.gelirgideruygulamas.main.common.constant.ExpenseCardSituation
import com.example.gelirgideruygulamas.main.common.constant.ExpenseType
import com.example.gelirgideruygulamas.main.common.util.Message
import com.example.gelirgideruygulamas.main.common.util.getCardType
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FragmentExpense(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton) :
    Fragment() {

    private lateinit var binding: FragmentExpenseBinding
    private lateinit var bindingDialog: LayoutAddExpenseBinding
    private lateinit var expenseUndoneList: ArrayList<Expense>
    private lateinit var expenseDoneList: ArrayList<Expense>
    private lateinit var expenseList: ArrayList<Expense>
    private lateinit var adapter: ExpenseAdapter
    private var fullScreenDialog: Dialog? = null
    private lateinit var expenseViewModel: ExpenseViewModel
    private val message = Message(mContext)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExpenseBinding.bind(inflater.inflate(R.layout.fragment_expense, container, false))
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabButton()
        setRvExpense()

        try {
            val message = "Welcome to Kotlin Tutorials"
            message.toInt()
        } catch (e: java.lang.NumberFormatException) {
            Log.e("aa", e.message.toString())
        }

    }


    private fun setRvExpense() {

        binding.rvExpense.setHasFixedSize(true)
        binding.rvExpense.layoutManager = LinearLayoutManager(mContext)

        expenseList = ArrayList()
        expenseUndoneList = ArrayList()
        expenseDoneList = ArrayList()

        // expenseViewModel.add(Expense("asd", 24f, DateHelper.currentTime, false, false, null, null, false, Expense.NEED, DateHelper.currentTime, DateHelper.currentTime, 1))

        //   Log.e("aa","")

        expenseViewModel.readAllData.observe(viewLifecycleOwner, Observer {

            extractList(expenseViewModel.readSelectedData(mContext))
            adapter.setData(expenseDoneList, expenseUndoneList, expenseList)

        })

        adapter = ExpenseAdapter(
            mContext,
            mAppCompatActivity,
            this,
            fab_add
        )
        binding.rvExpense.adapter = adapter
    }

    private fun setFabButton() {
        // fab_add.animate(R.layout.layout_add_expense)
        fab_add.setOnClickListener {
            if (PageLocation(mContext).getValue() == R.id.itemExpense)
                fab_add.hide()
            setFullScreenDialogExpense()

        }
    }

    private fun setFullScreenDialogExpense() {
        if (fullScreenDialog == null) {


            fullScreenDialog = if (Helper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }

            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(StatedDate(mContext).getDateLong()).build()
            var mTimeInMillis: Long = StatedDate(mContext).getDateLong()

            bindingDialog = LayoutAddExpenseBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScale)
            fullScreenDialog?.show()


            //başlangıçta yapılanlar
            bindingDialog.layoutExpenseAddRepetationType2.isChecked = true
            bindingDialog.layoutExpenseAddLenderView.isVisible = false
            bindingDialog.layoutExpenseAddMonthlyView.isVisible = false
            bindingDialog.layoutExpenseAddDelete.isVisible = false
            bindingDialog.layoutExpenseAddDone.isVisible = false
            bindingDialog.layoutExpenseAddRepetitionView.isVisible = true
            bindingDialog.layoutExpenseAddTypeWant.isChecked = true
            bindingDialog.layoutExpenseAddLenderView.isVisible = false
            bindingDialog.layoutExpenseAddDate.text = mContext.getString(R.string.expense_day)


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
                    val expense = Expense(
                        bindingDialog.layoutExpenseAddExpenseName.text.toString(),
                        bindingDialog.layoutExpenseAddAmount.text.toString().toFloat(),
                        mTimeInMillis,
                        false,
                        bindingDialog.layoutExpenseAddTypeDebt.isChecked,
                        if (bindingDialog.layoutExpenseAddTypeDebt.isChecked) bindingDialog.layoutExpenseAddLender.text.toString() else null,
                        if (bindingDialog.layoutExpenseAddRepetationType2.isChecked) null else bindingDialog.layoutExpenseAddRepetation.text.toString().toInt(),
                        false,
                        if (bindingDialog.layoutExpenseAddTypeNeed.isChecked) ExpenseType.NEED else if (bindingDialog.layoutExpenseAddTypeDebt.isChecked) ExpenseType.DEBT else ExpenseType.WANT,
                        mTimeInMillis
                    )
                    expenseViewModel.add(expense)
                    fullScreenDialog?.cancel()
                    fullScreenDialog = null
                }
                else {
                    getString(R.string.warning_empty)
                }
            }

            bindingDialog.layoutExpenseAddDate.setOnClickListener {
                setDateTimePickerDialog()
            }

        }
    }

    private fun extractList(expenseList: List<Expense>) {
        expenseDoneList.clear()
        expenseUndoneList.clear()
        this.expenseList.clear()

        for (expense in expenseList) {
            when {
                expense.getCardType() == ExpenseCardSituation.ONCE -> {
                    this.expenseList.add(expense)
                }
                expense.getCardType() == ExpenseCardSituation.DONE -> {
                    expenseDoneList.add(expense)
                }
                else -> {
                    expenseUndoneList.add(expense)
                }
            }
        }
    }
}
