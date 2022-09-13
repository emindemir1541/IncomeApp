package com.example.gelirgideruygulamas.fragments.income

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.data.income.Income
import com.example.gelirgideruygulamas.data.income.IncomeViewModel
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.helper.DateHelper
import com.example.gelirgideruygulamas.helper.Helper.Companion.clearZero
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IncomeAdapter(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var incomeList = emptyList<Income>()
    private var selectedMode = false
    private var selectedCards = ArrayList<MaterialCardView>()
    private var selectedCardsID = ArrayList<Int>()
    private lateinit var incomeViewModel: IncomeViewModel

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

    inner class IncomeCard1(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var incomeName: TextView
        var incomeAmount: TextView
        var incomeDate: TextView
        var incomeID: TextView

        init {
            incomeID = view.findViewById(R.id.incomeID)
            cardView = view.findViewById(R.id.IncomeCardView)
            incomeName = view.findViewById(R.id.incomeName)
            incomeAmount = view.findViewById(R.id.incomeAmount)
            incomeDate = view.findViewById(R.id.incomeDate)
        }
    }

    inner class InvisibleCard(view: View) : RecyclerView.ViewHolder(view) {
        var mLayout: ConstraintLayout

        init {
            mLayout = view.findViewById(R.id.card_invisible_layout)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            incomeList.size + 1 -> 2
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mDateCard =
            DateCard(LayoutInflater.from(mContext).inflate(R.layout.card_date, parent, false))
        val mIncomeCard =
            IncomeCard1(LayoutInflater.from(mContext).inflate(R.layout.card_income, parent, false))
        val mInvisibleCard =
            InvisibleCard(
                LayoutInflater.from(mContext).inflate(R.layout.card_invisible, parent, false)
            )
        return when (viewType) {
            0 -> mDateCard
            1 -> mIncomeCard
            else -> mInvisibleCard
        }
    }

    override fun getItemCount(): Int {
        incomeViewModel = ViewModelProvider(mAppCompatActivity)[IncomeViewModel::class.java]
        //fazladan bir payDay kardı ve bir görünmez kart var
        return incomeList.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (position) {
            0 -> {
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
                    cardDate.dateButton.text = StatedDate(mContext).getMonth()
                    incomeViewModel.refreshData()
                }
                cardDate.selectedDateButton.setOnClickListener() {
                    setDateTimePicker(cardDate.dateButton)
                }
                cardDate.leftArrow.setOnClickListener {
                    val statedDate = StatedDate(mContext)
                    statedDate.subtractMonth()
                    cardDate.dateButton.text = statedDate.getMonth()
                    incomeViewModel.refreshData()
                }
                cardDate.rightArrow.setOnClickListener {
                    val statedDate = StatedDate(mContext)
                    statedDate.addMonth()
                    cardDate.dateButton.text = statedDate.getMonth()
                    incomeViewModel.refreshData()
                }
            }
            incomeList.size + 1 -> {
                // val cardInvisible = holder as InvisibleCard
            }
            else -> {

                val cardIncome = holder as IncomeCard1
                val card = incomeList[position - 1]
                cardIncome.incomeID.text = card.id.toString()
                cardIncome.incomeID.isVisible = false
                cardIncome.incomeName.text = card.name
                cardIncome.incomeDate.text = DateHelper.convertToString(card.localDateTime.dayOfMonth, card.localDateTime.monthValue, card.localDateTime.year)
                cardIncome.incomeAmount.text = card.amount!!.clearZero()

                //cardview basılı tutunca seçme
                cardIncome.cardView.setOnLongClickListener {
                    if (!selectedMode) {
                        cardIncome.cardView.isChecked = !cardIncome.cardView.isChecked
                        selectedCards.add(cardIncome.cardView)
                        selectedCardsID.add(cardIncome.incomeID.text.toString().toInt())
                        enterEditMode()
                    }
                    true
                }
                cardIncome.cardView.setOnClickListener {
                    if (selectedMode) {
                        if (!cardIncome.cardView.isChecked) {
                            cardIncome.cardView.isChecked = true
                            selectedCards.add(cardIncome.cardView)
                            selectedCardsID.add(cardIncome.incomeID.text.toString().toInt())
                        }
                        else {
                            cardIncome.cardView.isChecked = false
                            selectedCards.remove(cardIncome.cardView)
                            selectedCardsID.remove(cardIncome.incomeID.text.toString().toInt())
                        }
                    }
                    if (selectedCards.size == 0) {
                        selectedMode = false
                        editModeFabButtons(selectedMode)
                    }
                }
                fab_editMode_delete.setOnClickListener {
                    for (id in selectedCardsID) {
                        incomeViewModel.deleteIncome(id)
                    }
                    exitEditMode()
                }
                fab_editMode_back.setOnClickListener {
                    exitEditMode()
                }
            }
        }

    }

    private fun setDateTimePicker(button: Button) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(StatedDate(mContext).getDateLong())
                .build()

        datePicker.show(mAppCompatActivity.supportFragmentManager, "tag")

        datePicker.addOnPositiveButtonClickListener { timeInMillis ->
            StatedDate(mContext).setDate(timeInMillis)
            button.text = StatedDate(mContext).getMonth()
            incomeViewModel.refreshData()
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(income: List<Income>) {
        this.incomeList = income
        notifyDataSetChanged()
    }

    private fun enterEditMode() {
        selectedMode = true
        editModeFabButtons(selectedMode)
    }

    private fun exitEditMode() {
        selectedMode = false
        editModeFabButtons(selectedMode)
        for (card in selectedCards)
            card.isChecked = false
        selectedCards.clear()
        selectedCardsID.clear()
    }

    private fun editModeFabButtons(open: Boolean) {
        if (open) {
            fab_add.hide()
            fab_editMode_back.show()
            fab_editMode_delete.show()
        }
        else {
            fab_add.show()
            fab_editMode_back.hide()
            fab_editMode_delete.hide()
        }
    }
}