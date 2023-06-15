package com.emindev.expensetodolist.main.common.constant

sealed class Page(val route: String) {
    object ExpenseUpdate : Page("ExpenseUpdate")
    object Income : Page("Income")
    object IncomeAdd : Page("IncomeAdd")
    object IncomeUpdate : Page("IncomeUpdate")
    object PageContent : Page("PageContent")
    object MainPage : Page("MainPage")
    object ExpenseAdd : Page("ExpenseAdd")
}