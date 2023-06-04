package com.emindev.expensetodolist.main.common.constant

sealed class Page(val route: String) {
    object Income : Page("Income")
    object IncomeAdd : Page("IncomeAdd")
    object IncomeUpdate:Page("IncomeUpdate")

    object PageContent:Page("PageContent")
    object MainPage:Page("MainPage")
    object ExpenseAdd:Page("ExpenseAdd")
}