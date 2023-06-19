package com.emindev.expensetodolist.main.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emindev.expensetodolist.expense.data.room.ExpenseEvent
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.expense.ui.pages.ExpenseAddPage
import com.emindev.expensetodolist.expense.ui.pages.ExpenseUpdatePage
import com.emindev.expensetodolist.income.ui.pages.IncomeAddPage
import com.emindev.expensetodolist.income.ui.pages.IncomeUpdatePage
import com.emindev.expensetodolist.main.common.constant.Page
import com.emindev.expensetodolist.income.data.room.IncomeEvent
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.data.retrofit.CurrencyViewModel
import com.emindev.expensetodolist.main.data.update.RemoteRepository
import com.emindev.expensetodolist.main.data.viewmodel.FinanceViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel

@Composable
fun Navigation(mainViewModel: MainViewModel, incomeViewModel: IncomeViewModel, expenseViewModel: ExpenseViewModel,financeViewModel: FinanceViewModel, currencyViewModel:CurrencyViewModel,onIncomeEvent: (IncomeEvent) -> Unit, onExpenseEvent: (ExpenseEvent) -> Unit) {

    val context = LocalContext.current
    val remoteModel = RemoteRepository(context).remoteData.collectAsState()


    val updateShowed = remember {
        mutableStateOf(false)
    }


    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Page.PageContent.route) {

        composable(route = Page.PageContent.route) {
            PageContent(navController = navController, incomeViewModel, expenseViewModel, mainViewModel, financeViewModel, onIncomeEvent, onExpenseEvent,remoteModel,updateShowed,currencyViewModel)
        }


        composable(route = Page.IncomeAdd.route) {
            IncomeAddPage(navController, mainViewModel = mainViewModel, incomeViewModel = incomeViewModel, onEvent = onIncomeEvent)
        }
        composable(route = Page.IncomeUpdate.route) {
            IncomeUpdatePage(navController, mainViewModel = mainViewModel, incomeViewModel = incomeViewModel, onEvent = onIncomeEvent)
        }
        composable(route = Page.ExpenseAdd.route) {
            ExpenseAddPage(navController = navController, mainViewModel = mainViewModel, expenseViewModel = expenseViewModel, onEvent = onExpenseEvent)
        }
        composable(route = Page.ExpenseUpdate.route) {
            ExpenseUpdatePage(navController = navController, mainViewModel = mainViewModel, expenseViewModel = expenseViewModel, onEvent = onExpenseEvent)
        }
        composable(route = Page.Lock.route) {
            LockPage()
        }
        composable(route = Page.Update.route) {
            UpdatePage(forceUpdate = remoteModel.value.forceUpdate, navController = navController,updateShowed)
        }
    }

}