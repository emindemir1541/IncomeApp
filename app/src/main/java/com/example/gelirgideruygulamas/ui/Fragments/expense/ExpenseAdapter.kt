package com.example.gelirgideruygulamas.fragments.expense

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.data.expense.Expense
import com.example.gelirgideruygulamas.data.expense.ExpenseViewModel
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.LayoutAddExpenseBinding
import com.example.gelirgideruygulamas.common.feedback.Message
import com.example.gelirgideruygulamas.common.constant.DateUtil
import com.example.gelirgideruygulamas.common.constant.Helper
import com.example.gelirgideruygulamas.common.constant.Helper.Companion.clearZero
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExpenseAdapter(
    private val mContext: Context,
    private val mAppCompatActivity: AppCompatActivity,
    private val mFragment: Fragment,
    private val fab_add: FloatingActionButton,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bindingDialog: LayoutAddExpenseBinding
    private var fullScreenDialog: Dialog? = null
    private var datePicker: MaterialDatePicker<Long>? = null
    private var expenseItems = emptyList<Expense>()
    private var expenseUndoneItems = emptyList<Expense>()
    private var expenseDoneItems = emptyList<Expense>()
    private var expenseListAll = emptyList<Expense>()
    private fun expenseListByCardId(cardId: Long, _expenseListAll: List<Expense>) = _expenseListAll.filter { expense -> expense.cardId == cardId }

    private lateinit var expenseViewModel: ExpenseViewModel

    inner class DateCard(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var leftArrow: FrameLayout
        var rightArrow: FrameLayout
        var dateButton: Button
        var selectedDateButton: Button

        init {
            cardView = view.findViewById(R.id.card_date_mcard)
            leftArrow = view.findViewById(R.id.card_date_left_arrow)
            rightArrow = view.findViewById(R.id.card_date_right_arrow)
            dateButton = view.findViewById(R.id.card_date_button)
            selectedDateButton = view.findViewById(R.id.card_date_button_selected)
        }
    }

    inner class ExpenseCardUndone(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var expenseID: TextView
        var expenseName: TextView
        var expenseAmount: TextView
        var expenseDate: TextView
        var expenseRemainingDay: TextView
        var expenseCheckBox: MaterialCheckBox
        var expenseUndone_viewForColor: ConstraintLayout
        var expenseUndone_debt: TextView

        init {
            cardView = view.findViewById(R.id.expenseCardViewUndone)
            expenseID = view.findViewById(R.id.expenseUndone_ID)
            expenseName = view.findViewById(R.id.expenseUndoneExpenseName)
            expenseAmount = view.findViewById(R.id.expenseUndone_ExpenseAmount)
            expenseDate = view.findViewById(R.id.expenseUndoneDate)
            expenseRemainingDay = view.findViewById(R.id.expenseUndone_RemainingDay)
            expenseCheckBox = view.findViewById(R.id.expenseUndone_CheckBox)
            expenseUndone_viewForColor = view.findViewById(R.id.expenseUndone_viewForColor)
            expenseUndone_debt = view.findViewById(R.id.expenseUndone_debt)
        }
    }

    inner class LineCard(view: View) : RecyclerView.ViewHolder(view) {
        var mLayout: ConstraintLayout

        init {
            mLayout = view.findViewById(R.id.card_line_layout)
        }
    }

    inner class ExpenseCardDone(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var expenseID: TextView
        var expenseName: TextView
        var expenseAmount: TextView
        var expenseDate: TextView
        var expenseRemainingDay: TextView
        var expenseDone_debt: TextView

        init {
            cardView = view.findViewById(R.id.expenseCardViewDone)
            expenseID = view.findViewById(R.id.expenseDone_ID)
            expenseName = view.findViewById(R.id.expenseDoneExpenseName)
            expenseAmount = view.findViewById(R.id.expenseDone_ExpenseAmount)
            expenseDate = view.findViewById(R.id.expenseDoneDate)
            expenseRemainingDay = view.findViewById(R.id.expenseDone_RemainingDay)
            expenseDone_debt = view.findViewById(R.id.expenseDone_debt)

        }
    }

    inner class ExpenseCard(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var expenseID: TextView
        var expenseName: TextView
        var expenseAmount: TextView
        var expenseDate: TextView

        init {
            cardView = view.findViewById(R.id.expenseCardView)
            expenseID = view.findViewById(R.id.expense_ID)
            expenseName = view.findViewById(R.id.expenseCard_ExpenseName)
            expenseAmount = view.findViewById(R.id.expenseCard_ExpenseAmount)
            expenseDate = view.findViewById(R.id.expenseCard_ExpenseDate)

        }
    }

    inner class InvisibleCard(view: View) : RecyclerView.ViewHolder(view) {
        var mLayout: ConstraintLayout = view.findViewById(R.id.card_invisible_layout)
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            card0(position) -> 0 //DateCard
            card1(position) -> 1 //ExpenseUndoneCard
            card2(position) -> 2 //LineCard
            card3(position) -> 3 //ExpenseCardDone
            card4(position) -> 4 //ExpenseCard
            else -> 5
        } //InvisibleCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val mDateCard =
            DateCard(LayoutInflater.from(mContext).inflate(R.layout.card_date, parent, false))
        val mExpenseCardUndone = ExpenseCardUndone(
            LayoutInflater.from(mContext).inflate(R.layout.card_expense_undone, parent, false)
        )
        val mLineCard =
            LineCard(LayoutInflater.from(mContext).inflate(R.layout.card_line, parent, false))
        val mExpenseCardDone = ExpenseCardDone(
            LayoutInflater.from(mContext).inflate(R.layout.card_expense_done, parent, false)
        )
        val mExpenseCard =
            ExpenseCard(LayoutInflater.from(mContext).inflate(R.layout.card_expense, parent, false))
        val mInvisibleCard = InvisibleCard(
            LayoutInflater.from(mContext).inflate(R.layout.card_invisible, parent, false)
        )

        return when (viewType) {
            0 -> mDateCard
            1 -> mExpenseCardUndone
            2 -> mLineCard
            3 -> mExpenseCardDone
            4 -> mExpenseCard
            else -> mInvisibleCard
        }
    }

    override fun getItemCount(): Int {
        expenseViewModel = ViewModelProvider(mAppCompatActivity)[ExpenseViewModel::class.java]
        expenseViewModel.readAllData.observe(mFragment.viewLifecycleOwner, Observer {
            expenseListAll = it
        })

        return expenseUndoneItems.size + expenseDoneItems.size + expenseItems.size + 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when {
            card0(position) -> {//Tarih kartı

                val cardDate = holder as DateCard


                fun cardDateToday(isToday: Boolean) {
                    cardDate.dateButton.isVisible = !isToday
                    cardDate.selectedDateButton.isVisible = isToday
                    cardDate.dateButton.text = StatedDate(mContext).getMonth()
                    cardDate.selectedDateButton.text = StatedDate(mContext).getMonth()
                }

                cardDateToday(StatedDate(mContext).isToday())

                //Tarihi burada ayarla
                cardDate.dateButton.setOnClickListener {
                    StatedDate(mContext).setToday()
                    cardDateToday(StatedDate(mContext).isToday())
                    refreshData()
                }
                cardDate.selectedDateButton.setOnClickListener() {
                    setDateTimePickerCard(cardDate.dateButton)
                }
                cardDate.rightArrow.setOnClickListener {
                    StatedDate(mContext).addMonth()
                    cardDateToday(StatedDate(mContext).isToday())

                    refreshData()
                }
                cardDate.leftArrow.setOnClickListener {
                    StatedDate(mContext).subtractMonth()
                    cardDateToday(StatedDate(mContext).isToday())
                    refreshData()
                }
            }
            card1(position) -> { //Expense Undone Kartı

                val cardExpenseUndone = holder as ExpenseCardUndone
                val expense = expenseUndoneItems[position - 1]

                cardExpenseUndone.expenseID.text = expense.id.toString()
                cardExpenseUndone.expenseID.isVisible = false
                cardExpenseUndone.expenseName.text = expense.name
                cardExpenseUndone.expenseAmount.text = expense.amount.clearZero() + "₺"
                cardExpenseUndone.expenseDate.text = DateUtil.convertToString(expense.date)
                cardExpenseUndone.expenseRemainingDay.text = expense.remainingDay(mContext)
                cardExpenseUndone.expenseCheckBox.isChecked = expense.completed
                if (expense.itsTime) {
                    cardExpenseUndone.expenseRemainingDay.setTextColor(mContext.getColor(R.color.yellow_warning))
                }
                else {
                    cardExpenseUndone.expenseRemainingDay.setTextColor(mContext.getColor(R.color.black))

                }
                cardExpenseUndone.expenseUndone_debt.isVisible = expense.debt
                if (expense.debt) {
                    cardExpenseUndone.expenseUndone_debt.text = expense.lender
                }
                cardExpenseUndone.cardView.setOnLongClickListener {
                    setFullScreenDialogExpense(false, expense)
                    false
                }
                cardExpenseUndone.expenseCheckBox.setOnClickListener {
                    if (cardExpenseUndone.expenseCheckBox.isChecked) {
                        expense.completed = true
                        expenseViewModel.updateOne(expense)
                    }
                }
            }
            card2(position) -> { //LineCard

            }
            card3(position) -> { //ExpenseCardDone
                val cardExpenseDone = holder as ExpenseCardDone
                val expense = expenseDoneItems[position - (expenseUndoneItems.size + 2)]

                cardExpenseDone.expenseID.text = expense.id.toString()
                cardExpenseDone.expenseID.isVisible = false
                cardExpenseDone.expenseName.text = expense.name
                cardExpenseDone.expenseAmount.text = expense.amount.clearZero() + "₺"
                cardExpenseDone.expenseDate.text = DateUtil.convertToString(expense.date)
                cardExpenseDone.expenseDone_debt.isVisible = expense.debt
                cardExpenseDone.expenseRemainingDay.text = expense.remainingDay(mContext)
                cardExpenseDone.expenseRemainingDay.setTextColor(mContext.getColor(R.color.dark_green_warning))
                if (expense.debt) {
                    cardExpenseDone.expenseDone_debt.text = expense.lender
                }
                cardExpenseDone.cardView.setOnLongClickListener {
                    setFullScreenDialogExpense(true, expense)
                    false
                }
            }
            card4(position) -> { //ExpenseCard
                val cardExpense = holder as ExpenseCard
                val expense = expenseItems[position - (expenseUndoneItems.size + expenseDoneItems.size + 2)]
                cardExpense.expenseID.text = expense.id.toString()
                cardExpense.expenseID.isVisible = false
                cardExpense.expenseAmount.text = expense.amount.clearZero() + "₺"
                cardExpense.expenseDate.text = DateUtil.convertToString(expense.date)
                cardExpense.expenseName.text = expense.name
                cardExpense.cardView.setOnLongClickListener {
                    setFullScreenDialogExpense(false, expense)
                    false
                }
            }
            else -> { //InvisibleCard
                val cardInvisible = holder as InvisibleCard
                cardInvisible.mLayout.isVisible = expenseUndoneItems.isNotEmpty()
            }
        }
    }

    private fun setDateTimePickerCard(button: Button) {

        if (datePicker == null) {
            datePicker = MaterialDatePicker.Builder.datePicker().setSelection(StatedDate(mContext).getDateLong()).build()
            datePicker!!.show(mAppCompatActivity.supportFragmentManager, "tag")
        }

        datePicker!!.addOnPositiveButtonClickListener { timeInMillis ->
            StatedDate(mContext).setDate(timeInMillis)
            button.text = StatedDate(mContext).getMonth()
            refreshData()
        }

        datePicker!!.addOnCancelListener {
            datePicker = null
        }

    }


    private fun setFullScreenDialogExpense(checkButtonExists: Boolean, expense: Expense) {

        val oldExpense = expense

        if (fullScreenDialog == null) {


            fullScreenDialog = if (Helper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }

            bindingDialog = LayoutAddExpenseBinding.inflate(LayoutInflater.from(mContext))
            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScaleCenter)
            fullScreenDialog?.show()


            bindingDialog.layoutExpenseAddExpenseName.setText(oldExpense.name)
            bindingDialog.layoutExpenseAddAmount.setText(oldExpense.amount.toString())

            bindingDialog.layoutExpenseAddMonthlyView.isVisible = false
            bindingDialog.layoutExpenseAddRepetationType.isVisible = false
            bindingDialog.layoutExpenseAddExpenseType.isVisible = false
            bindingDialog.layoutExpenseAddLenderView.isVisible = false
            bindingDialog.layoutExpenseAddDone.isVisible = checkButtonExists

            fun emptySafe(): Boolean {
                return bindingDialog.layoutExpenseAddAmount.text.toString().isNotEmpty() ||
                        bindingDialog.layoutExpenseAddExpenseName.text.toString().isNotEmpty()
            }


            fullScreenDialog?.setOnCancelListener {
                fab_add.show()
                exitFullScreen()
            }

            bindingDialog.layoutExpenseAddClose.setOnClickListener {
                exitFullScreen()
            }

            bindingDialog.layoutExpenseAddSave.setOnClickListener {
                if (emptySafe()) {
                    val newExpense = Expense(
                        bindingDialog.layoutExpenseAddExpenseName.text.toString(),
                        bindingDialog.layoutExpenseAddAmount.text.toString().toFloat(),
                        oldExpense.startedDateLong,
                        oldExpense.completed,
                        oldExpense.debt,
                        oldExpense.lender,
                        oldExpense.repetition,
                        oldExpense.deleted,
                        oldExpense.type,
                        oldExpense.dateLong,
                        DateUtil.currentTime,
                        oldExpense.cardId,
                        oldExpense.id,
                    )
                    expenseViewModel.updateAll(newExpense, expenseListByCardId(newExpense.cardId, expenseListAll))
                    exitFullScreen()
                }
                else {
                    Message(mContext).warningEmpty()
                }
            }

            bindingDialog.layoutExpenseAddDelete.setOnClickListener {
                expenseViewModel.delete(oldExpense, expenseListByCardId(expense.cardId, expenseListAll))
                exitFullScreen()
            }

            bindingDialog.layoutExpenseAddDone.setOnClickListener {
                oldExpense.completed = false
                expenseViewModel.updateOne(oldExpense)
                exitFullScreen()
            }


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(expenseDoneItems: List<Expense>, expenseUndoneItems: List<Expense>, expenseItems: List<Expense>) {
        this.expenseDoneItems = expenseDoneItems
        this.expenseUndoneItems = expenseUndoneItems
        this.expenseItems = expenseItems
        notifyDataSetChanged()
    }

    private fun refreshData() {
        expenseViewModel.refreshData()
    }

    //liste bu kart aralıklarında ise true döndürür
    private fun card0(position: Int): Boolean = position == 0 //DateCard
    private fun card1(position: Int): Boolean = position > 0 && position <= expenseUndoneItems.size //ExpenseUndoneCard
    private fun card2(position: Int): Boolean = position == expenseUndoneItems.size + 1 //LineCard
    private fun card3(position: Int): Boolean = position > expenseUndoneItems.size + 1 && position <= expenseUndoneItems.size + 1 + expenseDoneItems.size //ExpenseCardDone
    private fun card4(position: Int): Boolean = position > expenseUndoneItems.size + 1 + expenseDoneItems.size && position <= expenseUndoneItems.size + 1 + expenseDoneItems.size + expenseItems.size //ExpenseCard

    private fun exitFullScreen() {
        fullScreenDialog?.cancel()
        fullScreenDialog = null
    }
}