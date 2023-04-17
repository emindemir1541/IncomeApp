package com.example.gelirgideruygulamas.expense.ui.fragment

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.expense.data.room.Expense
import com.example.gelirgideruygulamas.expense.data.room.ExpenseViewModel
import com.example.gelirgideruygulamas.databinding.LayoutAddExpenseBinding
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper.clearZero
import com.example.gelirgideruygulamas.main.common.constant.Currency
import com.example.gelirgideruygulamas.expense.common.constant.ExpenseCardType
import com.example.gelirgideruygulamas.expense.common.constant.ExpenseSituation
import com.example.gelirgideruygulamas.main.common.constant.TaggedCard
import com.example.gelirgideruygulamas.expense.common.util.getCardType
import com.example.gelirgideruygulamas.expense.common.util.remainingDay
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExpenseAdapter(
    private val mContext: Context,
    private val mAppCompatActivity: AppCompatActivity,
    private val mFragment: Fragment,
    private val fab_add: FloatingActionButton,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bindingDialog: LayoutAddExpenseBinding
    private var fullScreenDialog: Dialog? = null
    private var expenseListSelected = emptyList<TaggedCard<Expense>>()
    private var expenseListAll = emptyList<Expense>()
    private fun expenseListByCardId(cardId: Long, _expenseListAll: List<Expense>) = _expenseListAll.filter { expense -> expense.cardId == cardId }

    private lateinit var expenseViewModel: ExpenseViewModel


    /*inner class Date"Card(view: View) : RecyclerView.ViewHolder(view) {
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
    }*/

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
        return expenseListSelected[position].tag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

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
            ExpenseCardType.EXPENSE_UNDONE_CARD -> mExpenseCardUndone
            ExpenseCardType.LINE_CARD -> mLineCard
            ExpenseCardType.EXPENSE_DONE_CARD -> mExpenseCardDone
            ExpenseCardType.EXPENSE_DAILY_CARD -> mExpenseCard
            else -> mInvisibleCard
        }
    }

    override fun getItemCount(): Int {
        expenseViewModel = ViewModelProvider(mAppCompatActivity)[ExpenseViewModel::class.java]
        expenseViewModel.readAllData.observe(mFragment.viewLifecycleOwner) {
            expenseListAll = it
        }
        return expenseListSelected.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val expense = expenseListSelected[position].card

        if (expense != null) {

            when (expenseListSelected[position].tag) {

                ExpenseCardType.EXPENSE_UNDONE_CARD -> {

                    val cardExpenseUndone = holder as ExpenseCardUndone

                    cardExpenseUndone.expenseID.text = expense.id.toString()
                    cardExpenseUndone.expenseID.isVisible = false
                    cardExpenseUndone.expenseName.text = expense.name
                    cardExpenseUndone.expenseAmount.text = expense.amount.clearZero() + Currency.TL
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

                ExpenseCardType.EXPENSE_DONE_CARD -> {
                    val cardExpenseDone = holder as ExpenseCardDone

                    cardExpenseDone.expenseID.text = expense.id.toString()
                    cardExpenseDone.expenseID.isVisible = false
                    cardExpenseDone.expenseName.text = expense.name
                    cardExpenseDone.expenseAmount.text = expense.amount.clearZero() + Currency.TL
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

                ExpenseCardType.EXPENSE_DAILY_CARD -> {
                    val cardExpense = holder as ExpenseCard
                    cardExpense.expenseID.text = expense.id.toString()
                    cardExpense.expenseID.isVisible = false
                    cardExpense.expenseAmount.text = expense.amount.clearZero() + Currency.TL
                    cardExpense.expenseDate.text = DateUtil.convertToString(expense.date)
                    cardExpense.expenseName.text = expense.name
                    cardExpense.cardView.setOnLongClickListener {
                        setFullScreenDialogExpense(false, expense)
                        false
                    }
                }

            }
        }
        else {
            when (expenseListSelected[position].tag) {
                ExpenseCardType.INVISIBLE_CARD -> {
                    val cardInvisible = holder as InvisibleCard
                    cardInvisible.mLayout.isVisible = expenseListSelected.isNotEmpty()
                }

                ExpenseCardType.LINE_CARD -> {
                    val cardLine = holder as LineCard
                    cardLine.mLayout.isVisible = true
                    // TODO: make an algorithm
                }
            }
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

                    val newExpense = oldExpense.copy(
                        name = bindingDialog.layoutExpenseAddExpenseName.text.toString(),
                        amount = bindingDialog.layoutExpenseAddAmount.text.toString().toFloat()
                    )

                    expenseViewModel.updateAll(newExpense, expenseListByCardId(newExpense.cardId, expenseListAll))
                    exitFullScreen()
                }
                else {
                    mContext.getString(R.string.warning_empty)
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
    fun setData(expenseList: List<Expense>) {
        this.expenseListSelected = sortExpenseList(expenseList)
        notifyDataSetChanged()
    }

    private fun sortExpenseList(expenseList: List<Expense>): ArrayList<TaggedCard<Expense>> {
        val formattedExpenseCardList = ArrayList<TaggedCard<Expense>>()
        val expenseUndoneCardList = ArrayList<TaggedCard<Expense>>()
        val expenseDoneCardList = ArrayList<TaggedCard<Expense>>()
        val expenseCardListDaily = ArrayList<TaggedCard<Expense>>()

        for (expense in expenseList) {
            when {
                expense.getCardType() == ExpenseSituation.ONCE -> {
                    expenseCardListDaily.add(TaggedCard(ExpenseCardType.EXPENSE_DAILY_CARD, expense))
                }

                expense.getCardType() == ExpenseSituation.DONE -> {
                    expenseDoneCardList.add(TaggedCard(ExpenseCardType.EXPENSE_DONE_CARD, expense))
                }

                else -> {
                    expenseUndoneCardList.add(TaggedCard(ExpenseCardType.EXPENSE_UNDONE_CARD, expense))
                }
            }
        }

        formattedExpenseCardList.addAll(expenseUndoneCardList)
        formattedExpenseCardList.add(TaggedCard(ExpenseCardType.LINE_CARD))
        formattedExpenseCardList.addAll(expenseDoneCardList)
        formattedExpenseCardList.addAll(expenseCardListDaily)
        formattedExpenseCardList.add(TaggedCard(ExpenseCardType.INVISIBLE_CARD))
        return formattedExpenseCardList
    }

    /*   private fun List<TaggedCard<Expense>>.getList(tag: Int): List<TaggedCard<Expense>> {
           return this.filter { taggedCard -> taggedCard.tag == tag }
       }*/

    private fun exitFullScreen() {
        fullScreenDialog?.cancel()
        fullScreenDialog = null
    }
}