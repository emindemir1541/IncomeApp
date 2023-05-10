package com.emindev.expensetodolist.main.common.calculation

import android.content.Context
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.expense.data.room.Expense
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.income.data.room.Income
import com.emindev.expensetodolist.main.data.sharedPreference.SavedMoney

class MonthlyCalculator(private val selectedIncomeList: List<Income>, private val selectedExpenseList: List<Expense>, private val mContext: Context) {

    val totalIncome: Float
        get() {
            var totalIncome = 0f
            selectedIncomeList.forEach { income ->
                if (income.itsTime)
                    totalIncome += income.amount
            }
            return totalIncome
        }

    val paidExpense: Float
        get() {
            var totalExpense = 0f
            selectedExpenseList.forEach { expense ->
                if ( expense.completed || expense.repetition == null)
                    totalExpense += expense.amount
            }
            return totalExpense
        }

    val potentialExpense: Float
        get() {
            var totalExpense = 0f
            selectedExpenseList.filter { expense: Expense -> expense.date.checkMonthAndYear(DateUtil.currentDateTime) }.forEach { expense ->
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