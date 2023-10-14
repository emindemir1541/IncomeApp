package com.emindev.expensetodolist.main.data.viewmodel

import androidx.lifecycle.ViewModel
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.expense.data.room.Expense
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.income.data.room.Income
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.model.FinanceModel
import kotlinx.coroutines.flow.combine

class FinanceViewModel(private val incomeViewModel: IncomeViewModel, private val expenseViewModel: ExpenseViewModel) :
    ViewModel() {

    val finance = incomeViewModel.allIncomesNotDeletedBySelectedDate.combine(expenseViewModel.allExpensesNotDeletedBySelectedDate) { incomes, expenses ->
        return@combine calculateFinance(incomes, expenses)
    }

    fun calculateFinance(incomeList: List<Income>, expenseList: List<Expense>): FinanceModel {

        var totalIncome = 0f
        var totalExpense = 0f
        var totalDebt = 0f
        var totalWish = 0f
        var totalNeed = 0f
        var paidExpense = 0f

        incomeList.forEach { income: Income ->
            totalIncome += income.latestAmount
        }
        expenseList.forEach { expense: Expense ->
            totalExpense += expense.latestAmount
            if (expense.repeatType == RepeatType.ONCE || expense.completed)
                paidExpense += expense.latestAmount
            when (expense.expenseType) {
                ExpenseType.NEED -> totalNeed += expense.latestAmount
                ExpenseType.UNNECESSARY -> totalWish += expense.latestAmount
                ExpenseType.DEBT -> totalDebt += expense.latestAmount
            }
        }
        return FinanceModel(remainedMoney = totalIncome - paidExpense, totalIncome = totalIncome, totalExpense = totalExpense, totalDebt = totalDebt, totalWish = totalWish, totalNeed = totalNeed, paidExpense = paidExpense, potentialRemainedMoney =totalIncome-totalExpense )
    }

}