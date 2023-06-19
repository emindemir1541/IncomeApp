package com.emindev.expensetodolist.main.common.constant

sealed class Page(val route: String) {
    object ExpenseUpdate : Page("ExpenseUpdate")
    object Income : Page("Income")
    object IncomeAdd : Page("IncomeAdd")
    object IncomeUpdate : Page("IncomeUpdate")
    object PageContent : Page("PageContent")
    object ExpenseAdd : Page("ExpenseAdd")

    object Lock:Page("Lock")
    object Update:Page("Update")

}