package com.example.gelirgideruygulamas.DataClass

data class Expense(var id:Int,var name:String,var amount:Float,var savedDate:String,var date:String,var done:Boolean,var debt:Boolean, var lender:String?,var repetition:Int?,var type:expenseType)