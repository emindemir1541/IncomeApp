package com.example.gelirgideruygulamas.fragments.income

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.main.data.income.Income
import com.example.gelirgideruygulamas.main.data.income.IncomeViewModel
import com.example.gelirgideruygulamas.main.data.sharedPreference.SavedMoney
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.LayoutAddIncomeBinding
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.monthString
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper.clearZero
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IncomeAdapter(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val mFragment: Fragment, private val fab_add: FloatingActionButton) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var incomeRepeatList = emptyList<Income>()
    private var incomeOnceList = emptyList<Income>()
    private var incomeListAll = emptyList<Income>()
    private lateinit var incomeViewModel: IncomeViewModel
    private var datePicker: MaterialDatePicker<Long>? = null
    private var fullScreenDialog: Dialog? = null
    private lateinit var bindingDialog: LayoutAddIncomeBinding
    private lateinit var savedMoney: SavedMoney
    private fun incomeListByCardId(cardId: Long, _incomeListAll: List<Income>) = _incomeListAll.filter { income -> income.cardId == cardId }


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

    inner class IncomeCardBig(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var incomeName: TextView
        var incomeAmount: TextView
        var incomeDate: TextView
        var incomeID: TextView
        var incomeRemainingDay: TextView

        init {
            incomeDate = view.findViewById(R.id.incomeDate)
            incomeID = view.findViewById(R.id.incomeID)
            cardView = view.findViewById(R.id.IncomeCardView)
            incomeName = view.findViewById(R.id.incomeName)
            incomeAmount = view.findViewById(R.id.incomeAmount)
            incomeRemainingDay = view.findViewById(R.id.incomeRemainingDay)
        }
    }

    inner class IncomeCardSmall(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: MaterialCardView
        var incomeName: TextView
        var incomeAmount: TextView
        var incomeDate: TextView
        var incomeID: TextView

        init {
            incomeID = view.findViewById(R.id.income_ID)
            cardView = view.findViewById(R.id.income_CardView)
            incomeName = view.findViewById(R.id.incomeCard_incomeName)
            incomeAmount = view.findViewById(R.id.incomeCard_incomeAmount)
            incomeDate = view.findViewById(R.id.incomeCard_incomeDate)
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
            card0(position) -> 0
            card1(position) -> 1
            card2(position) -> 2
            card3(position) -> 2
            else -> 3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mDateCard = DateCard(LayoutInflater.from(mContext).inflate(R.layout.card_date, parent, false))
        val mIncomeCardBig = IncomeCardBig(LayoutInflater.from(mContext).inflate(R.layout.card_income_big, parent, false))
        val mIncomeCardSmall = IncomeCardSmall(LayoutInflater.from(mContext).inflate(R.layout.card_income_small, parent, false))
        val mInvisibleCard = InvisibleCard(LayoutInflater.from(mContext).inflate(R.layout.card_invisible, parent, false))
        return when (viewType) {
            0 -> mDateCard
            1 -> mIncomeCardBig
            2 -> mIncomeCardSmall
            else -> mInvisibleCard
        }
    }

    override fun getItemCount(): Int {
        incomeViewModel = ViewModelProvider(mAppCompatActivity)[IncomeViewModel::class.java]
        incomeViewModel.readAllData.observe(mFragment.viewLifecycleOwner, Observer { incomeList ->
            incomeListAll = incomeList
        })
        savedMoney = SavedMoney(mContext)
        //fazladan bir dateLong kardı ve bir görünmez kart var
        return if (savedMoney.isPermanentEmpty()) incomeRepeatList.size + incomeOnceList.size + 2 else incomeRepeatList.size + incomeOnceList.size + 3
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when {
        card0(position) -> {
            val cardDate = holder as DateCard

            fun cardDateToday(isToday: Boolean) {
                cardDate.dateButton.isVisible = !isToday
                cardDate.selectedDateButton.isVisible = isToday
                cardDate.dateButton.text = StatedDate(mContext).month
                cardDate.selectedDateButton.text = StatedDate(mContext).month
            }
            cardDateToday(StatedDate(mContext).isToday)

            //Tarihi burada ayarla
            cardDate.dateButton.setOnClickListener {
                StatedDate(mContext).setToday()
                cardDateToday(StatedDate(mContext).isToday)
                incomeViewModel.refreshData()
            }
            cardDate.selectedDateButton.setOnClickListener() {
                setDateTimePicker(cardDate.dateButton)
            }
            cardDate.leftArrow.setOnClickListener {
                val statedDate = StatedDate(mContext)
                statedDate.subtractMonth()
                cardDateToday(statedDate.isToday)
                incomeViewModel.refreshData()
            }
            cardDate.rightArrow.setOnClickListener {
                val statedDate = StatedDate(mContext)
                statedDate.addMonth()
                cardDateToday(statedDate.isToday)
                incomeViewModel.refreshData()
            }
        }
        card1(position) -> {
            val cardIncome = holder as IncomeCardBig
            val card = incomeRepeatList[position - 1]
            cardIncome.incomeID.text = card.id.toString()
            cardIncome.incomeID.isVisible = false
            cardIncome.incomeRemainingDay.text = card.remainingDay(mContext)
            cardIncome.incomeName.text = card.name
            if (card.itsTime) cardIncome.incomeRemainingDay.setTextColor(mContext.getColor(R.color.dark_green_warning))
            else cardIncome.incomeRemainingDay.setTextColor(mContext.getColor(R.color.black))
            cardIncome.incomeDate.text = DateUtil.convertToString(card.date.dayOfMonth, card.date.monthValue, card.date.year)
            cardIncome.incomeAmount.text = card.amount.clearZero() + "₺"

            //cardview basılı tutunca seçme
            cardIncome.cardView.setOnLongClickListener {
                setFullScreenDialogIncome(card)
                true
            }
        }
        card2(position) -> {
            val cardIncome = holder as IncomeCardSmall
            val card = incomeOnceList[position - (incomeRepeatList.size + 1)]
            cardIncome.incomeID.text = card.id.toString()
            cardIncome.incomeID.isVisible = false
            cardIncome.incomeName.text = card.name
            cardIncome.incomeDate.text = DateUtil.convertToString(card.date.dayOfMonth, card.date.monthValue, card.date.year)
            cardIncome.incomeAmount.text = card.amount.clearZero() + "₺"

            //cardview basılı tutunca seçme
            cardIncome.cardView.setOnLongClickListener {
                setFullScreenDialogIncome(card)
                true
            }


        }
        card3(position) -> {
            val cardIncome = holder as IncomeCardSmall
            val date = DateUtil.currentDateTime.minusMonths(1)
            val savedMoney = SavedMoney(mContext).getPermanent()
            cardIncome.incomeID.isVisible = false
            cardIncome.incomeName.text = mContext.getString(R.string.money_in_cache)
            cardIncome.incomeDate.text = date.monthString() + " " + date.year.toString()
            cardIncome.incomeAmount.text = savedMoney.toString() + "₺"

            cardIncome.cardView.setOnLongClickListener {

                setFullScreenDialogRemainingMoney()
                true
            }
        }

        else -> {
            // val cardInvisible = holder as InvisibleCard


        }
    }

    private fun setFullScreenDialogIncome(income: Income) {
        if (fullScreenDialog == null) {


            fullScreenDialog = if (Helper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }

            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(income.dateLong).build()
            var mTimeInMillis: Long = StatedDate(mContext).dateLong

            bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScaleCenter)
            fullScreenDialog?.show()


            //başlangıçta yapılanlar
            bindingDialog.layoutIncomeAddDelete.isVisible = true
            bindingDialog.layoutIncomeAddRepetationType.isVisible = false
            bindingDialog.layoutIncomeAddMonthlyView.isVisible = income.repetation
            if (income.repetation) {
                bindingDialog.layoutIncomeAddDate.text = DateUtil.convertToString(income.date)
            }
            bindingDialog.layoutIncomeAddIncomeName.setText(income.name)
            bindingDialog.layoutIncomeAddAmount.setText(income.amount.toString())


            fun emptySafe(): Boolean {
                if (bindingDialog.layoutIncomeAddRepetationType1.isChecked) return false
                return bindingDialog.layoutIncomeAddAmount.text.toString().isNotEmpty() && bindingDialog.layoutIncomeAddIncomeName.text.toString().isNotEmpty()
            }

            fun setDateTimePickerDialog() {
                if (!datePicker.isVisible) {


                    datePicker.show(mAppCompatActivity.supportFragmentManager, "tag")

                    datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                        bindingDialog.layoutIncomeAddDate.text = DateUtil.convertToString(timeInMillis)
                        mTimeInMillis = timeInMillis
                    }
                }
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

                    val newIncome = Income(bindingDialog.layoutIncomeAddIncomeName.text.toString(), bindingDialog.layoutIncomeAddAmount.text.toString().toFloat(), income.startedDateLong, mTimeInMillis, false, income.repetation, income.dataChanged, income.cardId, income.id)
                    incomeViewModel.updateAll(newIncome, incomeListByCardId(newIncome.cardId, incomeListAll))
                    fullScreenDialog?.cancel()
                    fullScreenDialog = null
                }
                else {
                    mContext.getString(R.string.warning_empty)

                }
            }

            bindingDialog.layoutIncomeAddDate.setOnClickListener {
                setDateTimePickerDialog()
            }

            bindingDialog.layoutIncomeAddDelete.setOnClickListener {
                incomeViewModel.delete(income, incomeListByCardId(income.cardId, incomeListAll))
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setFullScreenDialogRemainingMoney() {
        val savedMoney = SavedMoney(mContext)

        if (fullScreenDialog == null) {


            fullScreenDialog = if (Helper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }


            bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScaleCenter)
            fullScreenDialog?.show()


            //başlangıçta yapılanlar
            bindingDialog.layoutIncomeAddDelete.isVisible = true
            bindingDialog.layoutIncomeAddRepetationType.isVisible = false
            bindingDialog.layoutIncomeAddMonthlyView.isVisible = false
            bindingDialog.layoutIncomeAddIncomeName.setText(mContext.getString(R.string.money_in_cache))
            bindingDialog.layoutIncomeAddAmount.setText(savedMoney.getPermanent().toString())


            fun emptySafe(): Boolean {
                return bindingDialog.layoutIncomeAddAmount.text.toString().isNotEmpty()
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


                    fullScreenDialog?.cancel()
                    fullScreenDialog = null
                }
                else {
                    mContext.getString(R.string.warning_empty)

                }
            }

            bindingDialog.layoutIncomeAddDelete.setOnClickListener {
                savedMoney.resetPermanent()
                notifyDataSetChanged()
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }
        }
    }

    private fun setDateTimePicker(button: Button) {

        if (datePicker == null) {
            datePicker = MaterialDatePicker.Builder.datePicker().setSelection(StatedDate(mContext).dateLong).build()

            datePicker!!.show(mAppCompatActivity.supportFragmentManager, "tag")
        }


        datePicker!!.addOnPositiveButtonClickListener { timeInMillis ->
            StatedDate(mContext).setDate(timeInMillis)
            button.text = StatedDate(mContext).month
            incomeViewModel.refreshData()
        }

        datePicker!!.addOnCancelListener {
            datePicker = null
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(incomeListOnce: List<Income>, incomeListRepeatable: List<Income>) {
        this.incomeOnceList = incomeListOnce
        this.incomeRepeatList = incomeListRepeatable
        notifyDataSetChanged()
    }

    //liste bu kart aralıklarında ise true döndürür
    private fun card0(position: Int): Boolean = position == 0
    private fun card1(position: Int): Boolean = position > 0 && position <= incomeRepeatList.size
    private fun card2(position: Int): Boolean = position > incomeRepeatList.size && position <= incomeRepeatList.size + incomeOnceList.size
    private fun card3(position: Int): Boolean = (!savedMoney.isPermanentEmpty()) && incomeRepeatList.size + incomeOnceList.size + 1 == position
}