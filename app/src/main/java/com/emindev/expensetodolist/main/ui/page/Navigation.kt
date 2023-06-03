package com.emindev.expensetodolist.main.ui.page

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emindev.expensetodolist.income.ui.pages.IncomeAddPage
import com.emindev.expensetodolist.income.ui.pages.IncomePage
import com.emindev.expensetodolist.income.ui.pages.IncomeUpdatePage
import com.emindev.expensetodolist.main.common.constant.Page
import com.emindev.expensetodolist.main.data.room.income.IncomeEvent
import com.emindev.expensetodolist.main.data.room.income.IncomeViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel

@Composable
fun Navigation(mainViewModel: MainViewModel, incomeViewModel: IncomeViewModel, onIncomeEvent: (IncomeEvent) -> Unit) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Page.PageContent.route) {

        composable(route = Page.PageContent.route) { 
            PageContent(navController = navController,incomeViewModel,mainViewModel,onIncomeEvent)
        }


        composable(route = Page.IncomeAdd.route) {
            IncomeAddPage(navController,mainViewModel = mainViewModel, incomeViewModel =incomeViewModel,onEvent = onIncomeEvent )
        }
        composable(route = Page.IncomeUpdate.route) {
            IncomeUpdatePage(navController,mainViewModel = mainViewModel, incomeViewModel =incomeViewModel,onEvent = onIncomeEvent )
        }
    }

}