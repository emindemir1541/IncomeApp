package com.example.gelirgideruygulamas.Adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.example.gelirgideruygulamas.DataClass.Income
import com.example.gelirgideruygulamas.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RVincomeAdapter(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val itemList: List<Income>, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedMode = false
    private var SelectedCards = ArrayList<MaterialCardView>()

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
            itemList.size + 1 -> 2
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var mDateCard =
            DateCard(LayoutInflater.from(mContext).inflate(R.layout.card_date, parent, false))
        var mIncomeCard =
            IncomeCard1(LayoutInflater.from(mContext).inflate(R.layout.card_income, parent, false))
        var mInvisibleCard =
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
        //fazladan bir date kardı ve bir görünmez kart var
        return itemList.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (position) {
            0 -> {
                val cardDate = holder as DateCard
                cardDate.dateButton.text = "Nisan" // TODO: 17.03.2022 gerekli ayın tarihini getir


                //Tarihi burada ayarla
                holder.dateButton.setOnClickListener {
                    SetDateTimePicker()
                    Toast.makeText(mContext, "tarihi ayarla", Toast.LENGTH_SHORT).show()
                }
                holder.leftArrow.setOnClickListener {
                    Toast.makeText(mContext, "deneme", Toast.LENGTH_SHORT).show()
                }
                holder.rightArrow.setOnClickListener {
                    Toast.makeText(mContext, "deneme", Toast.LENGTH_SHORT).show()
                }
            }
            itemList.size + 1 -> {
                val cardInvisible = holder as InvisibleCard
            }
            else -> {
                val cardIncome = holder as IncomeCard1
                cardIncome.incomeID.text = itemList[position - 1].id.toString()
                cardIncome.incomeID.isVisible = false
                cardIncome.incomeName.text = itemList[position - 1].name
                cardIncome.incomeDate.text = itemList[position - 1].date
                cardIncome.incomeAmount.text = itemList[position - 1].amount.toString()

                //cardview basılı tutunca seçme
                holder.cardView.setOnLongClickListener {
                    if (!selectedMode) {
                        holder.cardView.setChecked(!holder.cardView.isChecked)
                        SelectedCards.add(holder.cardView)
                        selectedMode = true
                        EditModeFABbuttons(selectedMode)
                    }
                    true
                }
                holder.cardView.setOnClickListener() {
                    if (selectedMode) {
                        if (!holder.cardView.isChecked) {
                            holder.cardView.setChecked(true)
                            SelectedCards.add(holder.cardView)
                        }
                        else {
                            holder.cardView.setChecked(false)
                            SelectedCards.remove(holder.cardView)
                        }
                    }
                    if (SelectedCards.size == 0) {
                        selectedMode = false
                        EditModeFABbuttons(selectedMode)
                    }
                }
                fab_editMode_delete.setOnClickListener() {
                    for (item in SelectedCards) {
                        item.id // TODO: 14.03.2022 cardları temizle, sql den sil
                    }
                }
                fab_editMode_back.setOnClickListener {
                    selectedMode = false
                    EditModeFABbuttons(selectedMode)
                    for (card in SelectedCards)
                        card.setChecked(false)

                    SelectedCards.clear()
                }
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

    private fun EditModeFABbuttons(open: Boolean) {
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