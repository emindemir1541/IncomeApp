package com.emindev.expensetodolist.main.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emindev.expensetodolist.expense.data.room.Expense
import com.emindev.expensetodolist.expense.data.room.ExpenseCardModel
import com.emindev.expensetodolist.expense.data.room.ExpenseDao
import com.emindev.expensetodolist.expense.data.room.ExpenseModel
import com.emindev.expensetodolist.income.data.room.IncomeCardModel
import com.emindev.expensetodolist.income.data.room.IncomeDao
import com.emindev.expensetodolist.income.data.room.IncomeModel

@Database(entities = [ExpenseModel::class,ExpenseCardModel::class, IncomeModel::class, IncomeCardModel::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao

}
