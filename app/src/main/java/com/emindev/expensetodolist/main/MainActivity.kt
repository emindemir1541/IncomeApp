package com.emindev.expensetodolist.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.emindev.expensetodolist.BuildConfig
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.expense.data.room.Expense
import com.emindev.expensetodolist.expense.data.room.ExpenseEvent
import com.emindev.expensetodolist.helperlibrary.common.helper.SystemInfo
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.helperlibrary.common.model.Resource
import com.emindev.expensetodolist.helperlibrary.ui.alertDialogClassic
import com.emindev.expensetodolist.main.common.util.RemoteData
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.main.data.room.FinanceDatabase
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.data.update.RemoteRepository
import com.emindev.expensetodolist.main.data.update.RemoteSettings
import com.emindev.expensetodolist.main.ui.page.Navigation


class MainActivity : ComponentActivity() {


    private val financeDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceDatabase::class.java,
            "finance.db"
        ).build()
    }

    private val mainViewModel: MainViewModel by viewModels()
    private val expenseViewModel by viewModels<ExpenseViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExpenseViewModel(financeDatabase.expenseDao(), mainViewModel) as T
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }

        mainViewModel.appIsLoading

        SystemInfo.PACKAGE_NAME = packageName


        //updateCheck()


        setContent {


            val onExpenseEvent = expenseViewModel::onEvent
            val onIncomeEvent = incomeViewModel::onEvent



            //IncomeAddPage(DateUtil.localDateNow,incomeState = incomeState,onIncomeEvent,ChangeDataType.ADD)

            Navigation(mainViewModel = mainViewModel, incomeViewModel = incomeViewModel, expenseViewModel = expenseViewModel, onIncomeEvent = onIncomeEvent, onExpenseEvent = onExpenseEvent)

        }

        mainViewModel.appLoaded


    }


    private fun updateCheck() {

        fun remoteDialog(remote: RemoteData) {
            if (remote.isLocked) alertDialogClassic(getString(R.string.locked), getString(R.string.locked_content), false)
            else if (remote.hasUpdate) if (remote.forceUpdate) alertDialogClassic(getString(R.string.update), getString(R.string.update_content), false)
            else alertDialogClassic(getString(R.string.update), getString(R.string.update_force_content), getString(R.string.ok), {})
            addLog("RemoteSource", remote, "", "StartPage()")
            addLog("RemoteSource", BuildConfig.VERSION_CODE, "Current Version Code", "MainActivity")
        }

        RemoteRepository().remoteData {
            val remoteSettings = RemoteSettings(this)
            when (it) {
                is Resource.Success -> {
                    val remote = it.data!!
                    remoteDialog(remote)
                    remoteSettings.setRemoteData(remote)
                }

                is Resource.Error -> {
                    val remote = remoteSettings.getRemoteData()
                    remoteDialog(remote)
                }

                is Resource.Loading -> {

                }
            }
        }


    }


}
