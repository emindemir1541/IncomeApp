package com.emindev.expensetodolist.main.ui.page

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.expense.data.room.Expense
import com.emindev.expensetodolist.expense.data.room.ExpenseEvent
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.expense.ui.pages.ExpensePage
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.income.ui.pages.IncomePage
import com.emindev.expensetodolist.main.common.constant.BottomNavItems
import com.emindev.expensetodolist.main.common.constant.Page
import com.emindev.expensetodolist.income.data.room.IncomeEvent
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.component.DateRow
import com.emindev.expensetodolist.main.ui.component.HideAbleButtonContent
import com.emindev.expensetodolist.main.ui.component.isScrollingUp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageContent(navController: NavController, incomeViewModel: IncomeViewModel, expenseViewModel: ExpenseViewModel, mainViewModel: MainViewModel, onIncomeEvent: (IncomeEvent) -> Unit, onExpenseEvent: (ExpenseEvent) -> Unit) {

    val isContentOpen = remember { mutableStateOf(true) }


    val selectedPage = mainViewModel.bottomNavItem.collectAsState()
    val isCardCreating = mainViewModel.isCardCreating.collectAsState()
    val lazyColumnListState = rememberLazyListState()


    Scaffold(
        floatingActionButton = {
            HideAbleButtonContent(isVisibleBecauseOfScrolling = selectedPage.value != BottomNavItems.MainPage && lazyColumnListState.isScrollingUp()) {

                LargeFloatingActionButton(onClick = {
                    if (!isCardCreating.value) {
                        if (selectedPage.value == BottomNavItems.IncomePage)
                            navController.navigate(Page.IncomeAdd.route)
                        if (selectedPage.value == BottomNavItems.ExpensePage)
                            navController.navigate(Page.ExpenseAdd.route)
                    }
                    //  expenseViewModel.setState(Expense(1, 1, "ldsjfk", 45f, 4f, SqlDateUtil.convertDate(DateUtil.localDateNow), SqlDateUtil.convertDate(DateUtil.localDateNow), false, false, false, RepeatType.INFINITY, 5, ExpenseType.NEED, ""))
                    //  onExpenseEvent(ExpenseEvent.SaveExpense)
                }) {
                    if (isCardCreating.value)
                        CircularProgressIndicator()
                    else
                        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {

                NavItem(page = BottomNavItems.IncomePage, selected = selectedPage.value == BottomNavItems.IncomePage) {
                    mainViewModel.setNavItem(BottomNavItems.IncomePage)
                }

                NavItem(page = BottomNavItems.MainPage, selected = selectedPage.value == BottomNavItems.MainPage) {
                    mainViewModel.setNavItem(BottomNavItems.MainPage)


                }

                NavItem(page = BottomNavItems.ExpensePage, selected = selectedPage.value == BottomNavItems.ExpensePage) {
                    mainViewModel.setNavItem(BottomNavItems.ExpensePage)


                }
            }
        }
    ) { _ ->

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {

            AnimatedVisibility(visible = selectedPage.value != BottomNavItems.MainPage) {
                DateRow(modifier = Modifier.padding(vertical = 16.dp), mainViewModel = mainViewModel)
            }

            Box(modifier = Modifier
                .fillMaxSize()) {
                when (selectedPage.value) {
                    BottomNavItems.ExpensePage -> ExpensePage(navController = navController, mainViewModel = mainViewModel, expenseViewModel = expenseViewModel, listState = lazyColumnListState, onEvent = onExpenseEvent)
                    BottomNavItems.IncomePage -> IncomePage(navController = navController, mainViewModel = mainViewModel, incomeViewModel = incomeViewModel, lazyColumnListState, onEvent = onIncomeEvent)
                    BottomNavItems.MainPage -> MainPage()
                }
            }
        }


    }

}

@Composable
private fun RowScope.NavItem(page: BottomNavItems, selected: Boolean, onclick: () -> Unit) {
    NavigationBarItem(
        selected = selected,
        onClick = onclick,
        label = {
            Text(
                text = stringResource(id = page.name),
                fontWeight = FontWeight.SemiBold,
            )
        },
        icon = {
            Icon(
                imageVector = page.icon,
                contentDescription = "${page.name} Icon",
            )
        }
    )
}
