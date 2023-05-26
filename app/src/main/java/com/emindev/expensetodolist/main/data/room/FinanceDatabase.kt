package com.emindev.expensetodolist.main.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Expense::class, Income::class], version = 1)
abstract class FinanceDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao():IncomeDao

}
