package com.example.gelirgideruygulamas.Adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.DataClass.Expense
import com.example.gelirgideruygulamas.MyHelper
import com.example.gelirgideruygulamas.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton

class RvExpenseAdapter(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val expenseItems: List<Expense>, private val expenseUndoneItems: List<Expense>, private val expenseDoneItems: List<Expense>, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedMode = false
    private var SelectedCards = ArrayList<MaterialCardView>()
    private var fullScreenDialog: Dialog? = null

    inner class DateCard(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var leftArrow: FrameLayout
        var rightArrow: FrameLayout
        var dateButton: Button

        init {
            cardView = view.findViewById(R.id.card_date_mcard)
            leftArrow = view.findViewById(R.id.card_date_left_arrow)
            rightArrow = view.findViewById(R.id.card_date_right_arrow)
            dateButton = view.findViewById(R.id.card_date_button)
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
        var mLayout: ConstraintLayout

        init {
            mLayout = view.findViewById(R.id.card_invisible_layout)
        }
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

        var mDateCard =
            DateCard(LayoutInflater.from(mContext).inflate(R.layout.card_date, parent, false))
        var mExpenseCardUndone = ExpenseCardUndone(
            LayoutInflater.from(mContext).inflate(R.layout.card_expense_undone, parent, false)
        )
        var mLineCard =
            LineCard(LayoutInflater.from(mContext).inflate(R.layout.card_line, parent, false))
        var mExpenseCardDone = ExpenseCardDone(
            LayoutInflater.from(mContext).inflate(R.layout.card_expense_done, parent, false)
        )
        var mExpenseCard =
            ExpenseCard(LayoutInflater.from(mContext).inflate(R.layout.card_expense, parent, false))
        var mInvisibleCard = InvisibleCard(
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
        return expenseUndoneItems.size + expenseDoneItems.size + expenseItems.size + 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            card0(position) -> {//Tarih kartı
                val cardDate = holder as DateCard
                cardDate.dateButton.text = "Nisan" // TODO: 17.03.2022 gerekli ayın tarihini getir

                //Tarihi burada ayarla
                holder.dateButton.setOnClickListener {
                    SetDateTimePicker() // TODO: 21.03.2022 datetime picekr ayarla
                    Toast.makeText(mContext, "tarihi ayarla", Toast.LENGTH_SHORT).show()
                }
                holder.leftArrow.setOnClickListener {
                    Toast.makeText(mContext, "deneme", Toast.LENGTH_SHORT).show()
                }
                holder.rightArrow.setOnClickListener {
                    Toast.makeText(mContext, "deneme", Toast.LENGTH_SHORT).show()
                }

            }
            card1(position) -> { //Expense Undone Kartı

                val cardExpenseUndone = holder as ExpenseCardUndone
                cardExpenseUndone.expenseID.text = expenseUndoneItems[position - 1].id.toString()
                cardExpenseUndone.expenseID.isVisible = false
                cardExpenseUndone.expenseName.text = expenseUndoneItems[position - 1].name
                cardExpenseUndone.expenseAmount.text =
                    expenseUndoneItems[position - 1].amount.toString()
                cardExpenseUndone.expenseDate.text = expenseUndoneItems[position - 1].date
                cardExpenseUndone.expenseCheckBox.isChecked = expenseUndoneItems[position - 1].done
                //cardExpenseUndone.expenseUndone_viewForColor.setBackgroundColor(Color.parseColor("#FF0000")) todo renklere göre işlem yap
                cardExpenseUndone.expenseUndone_debt.isVisible = true // TODO: 14.04.2022 sql den verileri al

                cardExpenseUndone.cardView.setOnLongClickListener() {
                    SetFullScreenDialogExpense(false)
                    false
                }
            }
            card2(position) -> { //LineCard

            }
            card3(position) -> { //ExpenseCardDone
                val cardExpenseDone = holder as ExpenseCardDone
                cardExpenseDone.expenseID.text =
                    expenseDoneItems[position - (expenseUndoneItems.size + 2)].id.toString()
                cardExpenseDone.expenseID.isVisible = false
                cardExpenseDone.expenseName.text =
                    expenseDoneItems[position - (expenseUndoneItems.size + 2)].name
                cardExpenseDone.expenseAmount.text =
                    expenseDoneItems[position - (expenseUndoneItems.size + 2)].amount.toString()
                cardExpenseDone.expenseDate.text =
                    expenseDoneItems[position - (expenseUndoneItems.size + 2)].date
                cardExpenseDone.expenseDone_debt.isVisible = true // TODO: 14.04.2022 sql den verileri al
                cardExpenseDone.cardView.setOnLongClickListener() {
                    SetFullScreenDialogExpense(true)
                    false
                }
            }
            card4(position) -> { //ExpenseCard
                val cardExpense = holder as ExpenseCard
                cardExpense.expenseID.text = expenseItems[position - (expenseUndoneItems.size + expenseDoneItems.size + 2)].id.toString()
                cardExpense.expenseID.isVisible = false
                cardExpense.expenseAmount.text = expenseItems[position - (expenseUndoneItems.size + expenseDoneItems.size + 2)].amount.toString()
                cardExpense.expenseDate.text = expenseItems[position - (expenseUndoneItems.size + expenseDoneItems.size + 2)].date
                cardExpense.expenseName.text = expenseItems[position - (expenseUndoneItems.size + expenseDoneItems.size + 2)].name
                cardExpense.cardView.setOnLongClickListener() {
                    SetFullScreenDialogExpense(false)
                    false
                }
            }
            else -> { //InvisibleCard
                val cardInvisible = holder as InvisibleCard
                cardInvisible.mLayout.isVisible = expenseUndoneItems.isNotEmpty()
            }
        }
    }

    private fun SetDateTimePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                // .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.show(mAppCompatActivity.supportFragmentManager, "tag")

        datePicker.addOnPositiveButtonClickListener {
            // TODO: 21.03.2022 positif buton seçildiğinde ayı getir, götür
        }


    }

    fun SetFullScreenDialogExpense(checkButtonExists: Boolean) {

        if (fullScreenDialog == null) {


            fullScreenDialog = if ( MyHelper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }

            fullScreenDialog?.setContentView(R.layout.layot_add_expense)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScaleCenter)
            fullScreenDialog?.show()

            val layout_expense_add_expenseAdd = fullScreenDialog?.findViewById<TextView>(R.id.layout_expense_add_expenseAdd)
            val layout_expense_add_close = fullScreenDialog?.findViewById<FrameLayout>(R.id.layout_expense_add_close)
            val layout_expense_add_save = fullScreenDialog?.findViewById<MaterialButton>(R.id.layout_expense_add_save)
            val layout_expense_add_repetationType = fullScreenDialog?.findViewById<RadioGroup>(R.id.layout_expense_add_repetationType)
            val layout_expense_add_repetationType1 = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_repetationType1)
            val layout_expense_add_repetationType2 = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_repetationType2)
            val layout_expense_add_amount = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_amount)
            val layout_expense_add_date = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_date)
            val layout_expense_add_incomeName = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_incomeName)
            val layout_expense_add_lender = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_lender)
            val layout_expense_add_monthlyView = fullScreenDialog?.findViewById<LinearLayout>(R.id.layout_expense_add_monthlyView)
            val layout_expense_add_lenderView = fullScreenDialog?.findViewById<ConstraintLayout>(R.id.layout_expense_add_lenderView)
            val layout_expense_add_repetition = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_repetition)
            val layout_expense_add_delete = fullScreenDialog?.findViewById<Button>(R.id.layout_expense_add_delete)
            val layout_expense_add_done = fullScreenDialog?.findViewById<Button>(R.id.layout_expense_add_done)
            val layout_expense_add_everyMonty = fullScreenDialog?.findViewById<MaterialCheckBox>(R.id.layout_expense_add_everyMonty)
            val layout_expense_add_repetitionView = fullScreenDialog?.findViewById<ConstraintLayout>(R.id.layout_expense_add_repetitionView)
            val layout_expense_add_expenseType = fullScreenDialog?.findViewById<RadioGroup>(R.id.layout_expense_add_expenseType)
            val layout_expense_add_typeNeed = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_typeNeed)
            val layout_expense_add_typeWant = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_typeWant)
            val layout_expense_add_typeDebt = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_typeDebt)



            layout_expense_add_repetationType2?.isChecked = true
            layout_expense_add_lenderView?.isVisible = false
            layout_expense_add_monthlyView?.isVisible = false
            layout_expense_add_done?.isVisible = checkButtonExists
            layout_expense_add_everyMonty!!.isChecked = true
            layout_expense_add_repetitionView?.isVisible = false
            layout_expense_add_typeWant?.isChecked = true
            layout_expense_add_lenderView?.isVisible = false


            layout_expense_add_everyMonty.setOnClickListener { layout_expense_add_repetitionView?.isVisible = !layout_expense_add_everyMonty.isChecked }

            layout_expense_add_expenseType!!.setOnCheckedChangeListener { _, _ ->
                if (layout_expense_add_typeNeed!!.isChecked) {
                    layout_expense_add_lenderView?.isVisible = false
                    // TODO: 17.04.2022 save sql
                }
                else if (layout_expense_add_typeWant!!.isChecked) {
                    layout_expense_add_lenderView?.isVisible = false
                    // TODO: 17.04.2022  sql save
                }
                else if (layout_expense_add_typeDebt!!.isChecked) {
                    layout_expense_add_lenderView?.isVisible = true
                    // TODO: 17.04.2022 save sql
                }
            }

            layout_expense_add_repetationType?.setOnCheckedChangeListener() { radioGroup, i ->
                if (layout_expense_add_repetationType1!!.isChecked) {
                    layout_expense_add_monthlyView!!.isVisible = true
                }
                if (layout_expense_add_repetationType2!!.isChecked) {
                    layout_expense_add_monthlyView!!.isVisible = false
                }
            }

            fullScreenDialog?.setOnCancelListener {
                fab_add.show()
                fullScreenDialog = null
            }

            layout_expense_add_close?.setOnClickListener {
                fullScreenDialog?.cancel()
                fullScreenDialog = null

            }

            layout_expense_add_save?.setOnClickListener {
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }


        }
    }

    //liste bu kart aralıklarında ise true döndürür
    private fun card0(position: Int): Boolean = position == 0 //DateCard
    private fun card1(position: Int): Boolean = position > 0 && position <= expenseUndoneItems.size //ExpenseUndoneCard
    private fun card2(position: Int): Boolean = position == expenseUndoneItems.size + 1 //LineCard
    private fun card3(position: Int): Boolean = position > expenseUndoneItems.size + 1 && position <= expenseUndoneItems.size + 1 + expenseDoneItems.size //ExpenseCardDone
    private fun card4(position: Int): Boolean = position > expenseUndoneItems.size + 1 + expenseDoneItems.size && position <= expenseUndoneItems.size + 1 + expenseDoneItems.size + expenseItems.size //ExpenseCard
    private fun card5(position: Int): Boolean = position > expenseUndoneItems.size + 1 + expenseDoneItems.size + expenseItems.size //InvisibleCard


}