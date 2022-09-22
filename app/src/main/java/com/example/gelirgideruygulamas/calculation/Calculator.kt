package com.example.gelirgideruygulamas.calculation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gelirgideruygulamas.data.expense.Expense
import com.example.gelirgideruygulamas.data.expense.ExpenseViewModel
import com.example.gelirgideruygulamas.data.income.Income
import com.example.gelirgideruygulamas.data.income.IncomeViewModel
import com.example.gelirgideruygulamas.helper.DateHelper
import java.time.LocalDate

class Calculator() {

    init {
    }

    fun totalIncomeInSpecificDate(incomeList:List<Income>): Float {
        var totalIncome = 0f
          val currentDate = DateHelper().currentDateTime.toLocalDate()
        incomeList.forEach { income ->
            if (income.date.dayOfMonth <= currentDate.dayOfMonth)
                totalIncome += income.amount
        }
        return totalIncome
    }

}