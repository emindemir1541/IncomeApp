package com.emindev.expensetodolist.main

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.emindev.expensetodolist.main.common.helper.SystemInfo
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.main.data.room.FinanceDatabase
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.util.CardCreator
import com.emindev.expensetodolist.main.data.retrofit.CurrencyViewModel
import com.emindev.expensetodolist.main.data.viewmodel.FinanceViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.page.Navigation
import com.emindev.expensetodolist.main.ui.theme.MainTheme


class MainActivity : ComponentActivity() {


    private val financeDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceDatabase::class.java,
            "finance.db"
        ).build()
    }

    private val mainViewModel :MainViewModel by viewModels()

    private val financeViewModel by viewModels<FinanceViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FinanceViewModel(incomeViewModel,expenseViewModel) as T
                }
            }
        }
    )
    private val incomeViewModel by viewModels<IncomeViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return IncomeViewModel(financeDatabase.incomeDao(), mainViewModel) as T
                }
            }
        }
    )
    private val expenseViewModel by viewModels<ExpenseViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExpenseViewModel(financeDatabase.expenseDao(), mainViewModel) as T
                }
            }
        }
    )

    private val currencyViewModel:CurrencyViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        mainViewModel.appIsLoading

        SystemInfo.PACKAGE_NAME = packageName

        //updateCheck()


        setContent {



            val onExpenseEvent = expenseViewModel::onEvent
            val onIncomeEvent = incomeViewModel::onEvent

            MainTheme(useDarkTheme = true) {
                Navigation(mainViewModel = mainViewModel, incomeViewModel = incomeViewModel, expenseViewModel = expenseViewModel,financeViewModel = financeViewModel, onIncomeEvent = onIncomeEvent, onExpenseEvent = onExpenseEvent,currencyViewModel=currencyViewModel)
            }

        }


        CardCreator(mainViewModel,incomeViewModel,expenseViewModel).runCreator

    }

    override fun onResume() {
        super.onResume()

    }



}
