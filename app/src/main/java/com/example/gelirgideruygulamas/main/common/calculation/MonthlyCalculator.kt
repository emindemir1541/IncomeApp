package com.example.gelirgideruygulamas.main.common.calculation

import android.content.Context
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.main.common.util.isSelected
import com.example.gelirgideruygulamas.main.data.expense.Expense
import com.example.gelirgideruygulamas.main.data.income.Income
import com.example.gelirgideruygulamas.main.data.sharedPreference.SavedMoney

class MonthlyCalculator(private val incomeList: List<Income>, private val expenseList: List<Expense>, private val mContext: Context) {

    val totalIncome: Float
        get() {
            var totalIncome = 0f
            incomeList.forEach { income ->
                if (income.isSelected(mContext) && income.itsTime)
                    totalIncome += income.amount
            }
            return totalIncome
        }

    val paidExpense: Float
        get() {
            var totalExpense = 0f
            expenseList.forEach { expense ->
                if (expense.isSelected(mContext) && expense.itsTime && expense.completed)
                    totalExpense += expense.amount
            }
            return totalExpense
        }

    val potentialExpense: Float
        get() {
            var totalExpense = 0f
            expenseList.forEach { expense ->
                if (expense.isSelected(mContext) && expense.itsTime)
                    totalExpense += expense.amount
            }
            return totalExpense
        }

    val potentialRemainedMoney: Float
        get() {
            return totalIncome - potentialExpense
        }

    val remainedMoney: Float
        get() {
            val mRemainedMoney = totalIncome - paidExpense
            if (mRemainedMoney != 0f)
                SavedMoney(mContext).setTemporary(mRemainedMoney, DateUtil.currentDateTime)
            return mRemainedMoney
        }

}