package com.emindev.expensetodolist.main.common.model

import android.content.Context
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.main.common.util.ColorUtil

data class FinanceModel(
    val remainedMoney:Float,
    val totalIncome:Float,
    val totalExpense:Float,
    val paidExpense:Float,
    val totalDebt:Float,
    val totalWish:Float,
    val totalNeed:Float
){
    fun getCardModelList(context:Context): List<CardModel> = listOf(
        CardModel(context.getString(R.string.remaining_money), listOf(remainedMoney.toString()), ColorUtil.randomColor),
        CardModel(context.getString(R.string.total_income), listOf(totalIncome.toString()), ColorUtil.randomColor),
        CardModel(context.getString(R.string.total_expense), listOf(totalExpense.toString()), ColorUtil.randomColor),
        CardModel(context.getString(R.string.paid_expense), listOf(paidExpense.toString()), ColorUtil.randomColor),
        CardModel(context.getString(R.string.debt), listOf(totalDebt.toString()), ColorUtil.randomColor),
        CardModel(context.getString(R.string.want), listOf(totalWish.toString()), ColorUtil.randomColor),
        CardModel(context.getString(R.string.need), listOf(totalNeed.toString()), ColorUtil.randomColor),
    )
}
