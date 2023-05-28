package com.emindev.expensetodolist.main.common.calculation

/*

class Calculator(private val selectedIncomeList: List<Income>, private val selectedExpenseList: List<Expense>, private val mContext: Context) {

    val totalIncome: Float
        get() {
            var totalIncome = 0f
            selectedIncomeList.forEach { income ->
                if (income.isMoneyPaid)
                    totalIncome += income.latestAmount
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
            selectedExpenseList.filter { expense: Expense -> expense.date.isMonthAndYearEqualTo(DateUtil.localDateTimeNow) }.forEach { expense ->
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
                SavedMoney(mContext).setTemporary(mRemainedMoney, DateUtil.localDateTimeNow)
            return mRemainedMoney
        }

}*/
