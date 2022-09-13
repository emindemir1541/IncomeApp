package com.example.gelirgideruygulamas.fragments.expense

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
import com.example.gelirgideruygulamas.calculation.creator.Creator
import com.example.gelirgideruygulamas.data.expense.Expense
import com.example.gelirgideruygulamas.data.expense.ExpenseCardType
import com.example.gelirgideruygulamas.data.expense.ExpenseViewModel
import com.example.gelirgideruygulamas.data.sharedPreference.PageLocation
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.FragmentExpenseBinding
import com.example.gelirgideruygulamas.databinding.LayoutAddExpenseBinding
import com.example.gelirgideruygulamas.feedback.Message
import com.example.gelirgideruygulamas.helper.DateHelper
import com.example.gelirgideruygulamas.helper.Helper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentExpense(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    Fragment() {

    private lateinit var binding: FragmentExpenseBinding
    private lateinit var bindingDialog: LayoutAddExpenseBinding
    private lateinit var expenseUndoneList: ArrayList<Expense>
    private lateinit var expenseDoneList: ArrayList<Expense>
    private lateinit var expenseList: ArrayList<Expense>
    private lateinit var adapter: ExpenseAdapter
    private var fullScreenDialog: Dialog? = null
    private lateinit var expenseViewModel: ExpenseViewModel
    var created = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExpenseBinding.bind(inflater.inflate(R.layout.fragment_expense, container, false))
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFabButton()
        setRvExpense()

      //  Creator(mAppCompatActivity, this).createCard()


    }

    private fun setRvExpense() {

        binding.rvExpense.setHasFixedSize(true)
        binding.rvExpense.layoutManager = LinearLayoutManager(mContext)

        expenseList = ArrayList()
        expenseUndoneList = ArrayList()
        expenseDoneList = ArrayList()

        //expenseViewModel.addExpense(Expense("asd", 24f, DateHelper.currentTime, false, false, null, null, false, Expense.NEED, DateHelper.currentTime, DateHelper.currentTime, 1))

     //   Log.e("aa","")

        expenseViewModel.readAllData.observe(viewLifecycleOwner, Observer {

            extractList(expenseViewModel.readSelectedData(mContext))
            adapter.setData(expenseDoneList, expenseUndoneList, expenseList)

        })

        adapter = ExpenseAdapter(
            mContext,
            mAppCompatActivity,
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

            bindingDialog = LayoutAddExpenseBinding.inflate(LayoutInflater.from(context))

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
            bindingDialog.layoutExpenseAddEveryMonty.isChecked = true
            bindingDialog.layoutExpenseAddRepetitionView.isVisible = false
            bindingDialog.layoutExpenseAddTypeWant.isChecked = true
            bindingDialog.layoutExpenseAddLenderView.isVisible = false
            bindingDialog.layoutExpenseAddDate.text = StatedDate(mContext).getDateString()


            fun emptySafe(): Boolean {
                if (bindingDialog.layoutExpenseAddRepetationType1.isChecked && bindingDialog.layoutExpenseAddDate.text.toString().isEmpty()) return false
                if (bindingDialog.layoutExpenseAddTypeDebt.isChecked && bindingDialog.layoutExpenseAddLender.text.toString().isEmpty()) return false
                if (!bindingDialog.layoutExpenseAddEveryMonty.isChecked && bindingDialog.layoutExpenseAddRepetation.text.toString().isEmpty()) return false
                return bindingDialog.layoutExpenseAddAmount.text.toString().isNotEmpty() &&
                        bindingDialog.layoutExpenseAddExpenseName.text.toString().isNotEmpty()
            }

            fun setDateTimePickerDialog() {
                if (!datePicker.isVisible) {

                    datePicker.show(mAppCompatActivity.supportFragmentManager, "tag")

                    datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                        bindingDialog.layoutExpenseAddDate.text = DateHelper.convertToString(timeInMillis)
                        mTimeInMillis = timeInMillis
                    }
                }
                // TODO: 7.08.2022 datetime pickerde geçmiş aylar seçilmesin
            }

            bindingDialog.layoutExpenseAddRepetation.addTextChangedListener {
                val repetition = bindingDialog.layoutExpenseAddRepetation.text
                if (repetition!!.isNotEmpty() && repetition.toString().toInt() > 24) {
                    repetition.clear()
                    Message(mContext).muchCharacterWarning(24)
                }
            }

            bindingDialog.layoutExpenseAddEveryMonty.setOnClickListener { bindingDialog.layoutExpenseAddRepetitionView.isVisible = !bindingDialog.layoutExpenseAddEveryMonty.isChecked }

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
                        StatedDate(mContext).getDateLong(),
                        false,
                        bindingDialog.layoutExpenseAddTypeDebt.isChecked,
                        if (bindingDialog.layoutExpenseAddTypeDebt.isChecked) bindingDialog.layoutExpenseAddLender.text.toString() else null,
                        if (bindingDialog.layoutExpenseAddRepetationType2.isChecked) null else if (bindingDialog.layoutExpenseAddEveryMonty.isChecked) 0 else bindingDialog.layoutExpenseAddRepetation.text.toString().toInt(),
                        false,
                        if (bindingDialog.layoutExpenseAddTypeNeed.isChecked) Expense.NEED else if (bindingDialog.layoutExpenseAddTypeDebt.isChecked) Expense.DEBT else Expense.WANT,
                        mTimeInMillis
                    )
                    expenseViewModel.addExpense(expense)
                    fullScreenDialog?.cancel()
                    fullScreenDialog = null
                }
                else {
                    Message(mContext).emptyWarning()
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
                expense.getCardType() == ExpenseCardType.ONCE -> {
                    this.expenseList.add(expense)
                }
                expense.getCardType() == ExpenseCardType.DONE -> {
                    expenseDoneList.add(expense)
                }
                else -> {
                    expenseUndoneList.add(expense)
                }
            }
        }
    }
}
