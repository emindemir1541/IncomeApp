package com.emindev.expensetodolist.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.emindev.expensetodolist.BuildConfig
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.databinding.ActivityMainBinding
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.SystemInfo
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.helperlibrary.common.model.Resource
import com.emindev.expensetodolist.helperlibrary.ui.alertDialogClassic
import com.emindev.expensetodolist.main.common.util.RemoteData
import com.emindev.expensetodolist.main.data.room.expense.ExpenseEvent
import com.emindev.expensetodolist.main.data.room.expense.ExpenseViewModel
import com.emindev.expensetodolist.main.data.room.FinanceDatabase
import com.emindev.expensetodolist.main.data.room.income.IncomeEvent
import com.emindev.expensetodolist.main.data.room.income.IncomeViewModel
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.data.update.RemoteRepository
import com.emindev.expensetodolist.main.data.update.RemoteSettings


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var composeView: ComposeView

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

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ComposeView(applicationContext).also { composeView = it })


        //updateCheck()

        //CardCreator.e incomeCreator(incomeViewModel)

        composeView.setContent {

            val expenseState by expenseViewModel.state.collectAsState()
            val incomeState by incomeViewModel.state.collectAsState()

            val onExpenseEvent = expenseViewModel::onEvent
            val onIncomeEvent = incomeViewModel::onEvent


            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)) {
                items(expenseState.expenses) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(text = it.name)
                        Text(text = it.amount.toString())
                    }
                }

                items(incomeState.incomes) {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.Green)
                        .clickable {
                            onIncomeEvent(IncomeEvent.DeleteIncome(it))
                        }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(text = it.name)
                        Text(text = it.amount.toString())

                    }
                }

               /* items(incomeUniqueList.value) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.Blue)
                        , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(text = it.cardId.toString())

                    }
                }*/

                item {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                        Button(onClick = {

                            onExpenseEvent(ExpenseEvent.ShowDialog)
                            onExpenseEvent(ExpenseEvent.SetName("emin"))
                            onExpenseEvent(ExpenseEvent.SetAmount(23432f))
                            onExpenseEvent(ExpenseEvent.SetRepetition(3))
                            onExpenseEvent(ExpenseEvent.SetDay(4))
                            onExpenseEvent(ExpenseEvent.SetMonth(5))
                            onExpenseEvent(ExpenseEvent.SetYear(2023))
                            onExpenseEvent(ExpenseEvent.SetStartedDate(DateUtil.currentTime))
                            onExpenseEvent(ExpenseEvent.SetCompleted(false))
                            onExpenseEvent(ExpenseEvent.SetExpenseType(ExpenseType.NEED))
                            onExpenseEvent(ExpenseEvent.SaveExpense)
                            onExpenseEvent(ExpenseEvent.HideDialog)

                            Toast.makeText(this@MainActivity, "workinh-g", Toast.LENGTH_LONG).show()

                        }) {
                            Text(text = "add Expense")
                        }

                        Button(onClick = {
                            onIncomeEvent(IncomeEvent.ShowDialog)
                            onIncomeEvent(IncomeEvent.SetName("emin"))
                            onIncomeEvent(IncomeEvent.SetAmount(564f))
                            onIncomeEvent(IncomeEvent.SetInitialDate(DateUtil.currentDateTime.toLocalDate()))
                            onIncomeEvent(IncomeEvent.SetIsRepeatable(true))
                            onIncomeEvent(IncomeEvent.SetCurrentDate(/*DateUtil.convertToDateTime(2023, Random.nextInt(1,12),5).toLocalDate()*/DateUtil.currentDateTime.toLocalDate()))
                            onIncomeEvent(IncomeEvent.SaveIncome)
                            onIncomeEvent(IncomeEvent.HideDialog)
                        }) {
                            Text(text = "Add Income")
                        }

                    }

                    Button(onClick = {
                        mainViewModel.previousMonth
                    }) {
                        Text(text = "Back")
                    }

                    Button(onClick = {
                        mainViewModel.nextMonth
                    }) {
                        Text(text = "next")
                    }

                }

            }

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
