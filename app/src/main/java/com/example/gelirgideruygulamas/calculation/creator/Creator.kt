package com.example.gelirgideruygulamas.calculation.creator

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gelirgideruygulamas.data.expense.Expense
import com.example.gelirgideruygulamas.data.expense.ExpenseViewModel
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.helper.DateHelper
import java.time.LocalDateTime

class Creator(private val mAppCompatActivity: AppCompatActivity,private val mFragment:Fragment) {

    private var expenseViewModel: ExpenseViewModel = ViewModelProvider(mAppCompatActivity)[ExpenseViewModel::class.java]


    fun createCard(){
        expenseViewModel.readAllData.observe(mFragment.viewLifecycleOwner, Observer {expenseAllList->

        })
    }

    fun regulator(expenseList: List<Expense>){


    expenseList
    }



}