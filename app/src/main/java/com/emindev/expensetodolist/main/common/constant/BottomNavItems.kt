package com.emindev.expensetodolist.main.common.constant

import com.emindev.expensetodolist.R

sealed class BottomNavItems(val name: Int, val route: String,val  drawableIcon: Int) {
    object IncomePage :
        BottomNavItems(R.string.income, "incomePage", drawableIcon = R.drawable.png_income)

    object MainPage :
        BottomNavItems(R.string.main_page, "mainPage", drawableIcon = R.drawable.png_home)

    object ExpensePage :
        BottomNavItems(R.string.expense, "expensePage", drawableIcon = R.drawable.png_expense)
}

