package com.emindev.expensetodolist.main.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emindev.expensetodolist.main.data.room.expense.Expense
import com.emindev.expensetodolist.main.data.room.expense.ExpenseDao
import com.emindev.expensetodolist.main.data.room.income.Income
import com.emindev.expensetodolist.main.data.room.income.IncomeCardModel
import com.emindev.expensetodolist.main.data.room.income.IncomeDao
import com.emindev.expensetodolist.main.data.room.income.IncomeModel

@Database(entities = [Expense::class, IncomeModel::class,IncomeCardModel::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao

}
