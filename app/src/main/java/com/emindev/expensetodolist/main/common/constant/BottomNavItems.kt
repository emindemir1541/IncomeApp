package com.emindev.expensetodolist.main.common.constant

import com.emindev.expensetodolist.R

sealed class BottomNavItems(val name: Int,val  drawableIcon: Int) {
    object IncomePage :
        BottomNavItems(R.string.income,  drawableIcon = R.drawable.png_income)

    object MainPage :
        BottomNavItems(R.string.main_page, drawableIcon = R.drawable.png_home)

    object ExpensePage :
        BottomNavItems(R.string.expense, drawableIcon = R.drawable.png_expense)
}

