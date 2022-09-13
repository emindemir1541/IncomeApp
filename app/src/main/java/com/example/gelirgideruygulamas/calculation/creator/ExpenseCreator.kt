package com.example.gelirgideruygulamas.calculation.creator

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gelirgideruygulamas.data.expense.Expense
import com.example.gelirgideruygulamas.data.expense.ExpenseCardType
import com.example.gelirgideruygulamas.data.expense.ExpenseViewModel
import com.example.gelirgideruygulamas.data.expense.SavedDate
import com.example.gelirgideruygulamas.helper.DateHelper
import com.example.gelirgideruygulamas.helper.DateHelper.Companion.checkMonthAndYear
import java.lang.Error
import java.time.LocalDateTime

class ExpenseCreator(private val expenseViewModel: ExpenseViewModel,private val expenseListAll:List<Expense>,private val savedDates:List<SavedDate>) {


private val createdCardList:ArrayList<Expense> = ArrayList()

    fun create():ArrayList<Expense> {
        cardDeckList().forEach { expenseList ->
            if (!expenseList.isNullOrEmpty()) {

                val oneExpense = expenseList[0]

                if (oneExpense.repetition != 0 && oneExpense.repetition != null) {
                    for (repetition in 1..oneExpense.repetition!!) {
                        val newLocalDate = oneExpense.localDateTime.plusMonths(repetition.toLong())
                        var containsCard = false
                        expenseList.forEach {expense->
                            if (newLocalDate.checkMonthAndYear(expense.localDateTime))
                                containsCard = true
                        }
                        if (!containsCard){
                            createCard(oneExpense,newLocalDate)
                        }
                    }
                }
            }
        }
return createdCardList
    }

    private fun createCard(expense: Expense, localDate: LocalDateTime) {
            val newExpense = expense
            newExpense.dateTime = DateHelper.toLong(localDate)
            createdCardList.add(newExpense)
    }



    private fun cardDeckList(): ArrayList<List<Expense>> {
        val cardDeckList = ArrayList<List<Expense>>()
        val expenseList = expenseListAll
        savedDates.forEach { savedDate ->
            val oneCardDeck = expenseList.filter { expense: Expense -> expense.savedDateTime == savedDate.savedDateTime && !expense.deleted && expense.getCardType() != ExpenseCardType.ONCE }.sortedBy { expense: Expense -> expense.dateTime }
            cardDeckList.add(oneCardDeck)
        }
        return cardDeckList
    }

}

/*dateTimesWithoutCreatedCards(savedDate, cardsWithOneSavedDate).forEach { dateTime ->
    createCard(cardsWithOneSavedDate[0], dateTime)
}*/