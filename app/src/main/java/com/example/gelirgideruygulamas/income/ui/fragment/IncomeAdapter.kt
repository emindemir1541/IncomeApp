package com.example.gelirgideruygulamas.income.ui.fragment

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.income.data.room.Income
import com.example.gelirgideruygulamas.income.data.room.IncomeViewModel
import com.example.gelirgideruygulamas.main.data.sharedPreference.SavedMoney
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.databinding.LayoutAddIncomeBinding
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.monthString
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.toLong
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper.clearZero
import com.example.gelirgideruygulamas.income.common.IncomeCardType
import com.example.gelirgideruygulamas.income.common.remainingDay
import com.example.gelirgideruygulamas.income.ui.component.DialogUtil
import com.example.gelirgideruygulamas.main.common.constant.TaggedCard
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class IncomeAdapter(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val mFragment: Fragment, private val fab_add: FloatingActionButton) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var incomeSelectedList = emptyList<TaggedCard<Income>>()
    private var incomeListAll = emptyList<Income>()
    private lateinit var incomeViewModel: IncomeViewModel
    private var fullScreenDialog: Dialog? = null
    private lateinit var bindingDialog: LayoutAddIncomeBinding
    private lateinit var savedMoney: SavedMoney
    private fun incomeListByCardId(cardId: Long, _incomeListAll: List<Income>) = _incomeListAll.filter { income -> income.cardId == cardId }


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
        return incomeSelectedList[position].tag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mIncomeCardBig = IncomeCardBig(LayoutInflater.from(mContext).inflate(R.layout.card_income_big, parent, false))
        val mIncomeCardSmall = IncomeCardSmall(LayoutInflater.from(mContext).inflate(R.layout.card_income_small, parent, false))
        val mInvisibleCard = InvisibleCard(LayoutInflater.from(mContext).inflate(R.layout.card_invisible, parent, false))
        return when (viewType) {
            IncomeCardType.REPEATABLE_CARD -> mIncomeCardBig
            IncomeCardType.ONCE_CARD -> mIncomeCardSmall
            IncomeCardType.PERMANENT_CARD -> mIncomeCardSmall
            else -> mInvisibleCard
        }
    }

    override fun getItemCount(): Int {
        incomeViewModel = ViewModelProvider(mAppCompatActivity)[IncomeViewModel::class.java]
        incomeViewModel.readAllData.observe(mFragment.viewLifecycleOwner) { incomeList ->
            incomeListAll = incomeList
        }
        return incomeSelectedList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val income = incomeSelectedList[position].card
        if (income != null) {

            when (incomeSelectedList[position].tag) {

                IncomeCardType.REPEATABLE_CARD -> {
                    val cardIncome = holder as IncomeCardBig
                    cardIncome.incomeID.text = income.id.toString()
                    cardIncome.incomeID.isVisible = false
                    cardIncome.incomeRemainingDay.text = income.remainingDay(mContext)
                    cardIncome.incomeName.text = income.name
                    if (income.itsTime) cardIncome.incomeRemainingDay.setTextColor(mContext.getColor(R.color.dark_green_warning))
                    else cardIncome.incomeRemainingDay.setTextColor(mContext.getColor(R.color.black))
                    cardIncome.incomeDate.text = DateUtil.convertToString(income.date.dayOfMonth, income.date.monthValue, income.date.year)
                    cardIncome.incomeAmount.text = income.amount.clearZero() + "₺"

                    //cardview basılı tutunca seçme
                    cardIncome.cardView.setOnLongClickListener {
                        setFullScreenDialogIncome(income)
                        true
                    }
                }

                IncomeCardType.ONCE_CARD -> {
                    val cardIncome = holder as IncomeCardSmall
                    cardIncome.incomeID.text = income.id.toString()
                    cardIncome.incomeID.isVisible = false
                    cardIncome.incomeName.text = income.name
                    cardIncome.incomeDate.text = DateUtil.convertToString(income.date.dayOfMonth, income.date.monthValue, income.date.year)
                    cardIncome.incomeAmount.text = income.amount.clearZero() + "₺"

                    cardIncome.cardView.setOnLongClickListener {
                        setFullScreenDialogIncome(income)
                        true
                    }


                }

                IncomeCardType.PERMANENT_CARD -> {
                    val cardIncome = holder as IncomeCardSmall
                    val date = DateUtil.currentDateTime.minusMonths(1)
                    val savedMoney = SavedMoney(mContext).permanentMoney
                    cardIncome.incomeID.isVisible = false
                    cardIncome.incomeName.text = mContext.getString(R.string.money_in_cache)
                    cardIncome.incomeDate.text = date.monthString() + " " + date.year.toString()
                    cardIncome.incomeAmount.text = savedMoney.toString() + "₺"

                    cardIncome.cardView.setOnLongClickListener {

                        setFullScreenDialogRemainingMoney()
                        true
                    }
                }

            }

        }
    }

    private fun setFullScreenDialogIncome(income: Income) {
        if (fullScreenDialog == null) {


            fullScreenDialog = DialogUtil.materialThemeDialog(mContext)

            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(income.date.toLong()).build()
            var mTimeInMillis: Long = StatedDate(mContext).dateLong

            bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScaleCenter)
            fullScreenDialog?.show()


            //başlangıçta yapılanlar
            bindingDialog.layoutIncomeAddDelete.isVisible = true
            bindingDialog.layoutIncomeAddRepetationType.isVisible = false
            bindingDialog.layoutIncomeAddMonthlyView.isVisible = income.isRepeatable
            if (income.isRepeatable) {
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

                    val date = DateUtil.convertToDateTime(mTimeInMillis)
                    val newIncome = income.copy(
                        name = bindingDialog.layoutIncomeAddIncomeName.text.toString(),
                        amount = bindingDialog.layoutIncomeAddAmount.text.toString().toFloat(),
                        day = date.dayOfMonth,
                        month = date.monthValue,
                        year = date.year
                    )
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

        if (fullScreenDialog == null) {


            fullScreenDialog = DialogUtil.materialThemeDialog(mContext)


            bindingDialog = LayoutAddIncomeBinding.inflate(LayoutInflater.from(mContext))

            fullScreenDialog?.setContentView(bindingDialog.root)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScaleCenter)
            fullScreenDialog?.show()


            bindingDialog.layoutIncomeAddDelete.isVisible = true
            bindingDialog.layoutIncomeAddRepetationType.isVisible = false
            bindingDialog.layoutIncomeAddMonthlyView.isVisible = false
            bindingDialog.layoutIncomeAddIncomeName.setText(mContext.getString(R.string.money_in_cache))
            bindingDialog.layoutIncomeAddAmount.setText(savedMoney.permanentMoney.toString())


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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(incomeList: List<TaggedCard<Income>>) {
        incomeSelectedList = incomeList
        notifyDataSetChanged()
    }

}