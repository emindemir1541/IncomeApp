package com.example.gelirgideruygulamas.calculation.creator

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gelirgideruygulamas.data.expense.Expense
import com.example.gelirgideruygulamas.data.expense.ExpenseCardType
import com.example.gelirgideruygulamas.data.expense.ExpenseViewModel

class Creator(private val mAppCompatActivity: AppCompatActivity, private val mFragment: Fragment) {

    private var expenseViewModel: ExpenseViewModel = ViewModelProvider(mAppCompatActivity)[ExpenseViewModel::class.java]


    fun createCard() {
        expenseViewModel.readAllData.observe(mFragment.viewLifecycleOwner, Observer { expenseAllList ->
            var currentCardList:ArrayList<Expense> = ArrayList()
            var currentCardId:Long = 0
            expenseAllList.filter { expense -> rule(expense) }.sortedBy { it.cardId }.forEach { expense ->
                if (currentCardId == 0L || currentCardId ==expense.cardId){
                   currentCardList.add(expense)
                }else{
                    currentCardId = expense.cardId

                    val newExpense = currentCardList[0]
                    repeat(newExpense.repetition!!-1){
                        newExpense.date = newExpense.startedDate.plusMonths(it.toLong())
                        if (!currentCardList.contains(newExpense))
                            expenseViewModel.addExpense(newExpense)
                    }

                    currentCardList.clear()
                }

            }
        })
    }


    fun regulator(expenseList: List<Expense>): ArrayList<List<Expense>> {
        var cardList = ArrayList<List<Expense>>()
        var cardIdList = ArrayList<Long>()
        expenseList.forEach { expense ->
            cardIdList.add(expense.cardId)
        }
        cardIdList.forEach { cardId ->
            var card = expenseList.filter { expense ->
                expense.cardId == cardId
            }
            cardList.add(card)
        }
        return cardList

    }

    //repetation 0 değil kuralı
    private fun rule(expense: Expense): Boolean = expense.repetition != null && expense.repetition != 0 && !expense.deleted && expense.getCardType() != ExpenseCardType.ONCE

    //repetation 0 kuralı
}