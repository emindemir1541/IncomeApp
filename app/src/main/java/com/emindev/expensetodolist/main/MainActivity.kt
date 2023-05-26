package com.emindev.expensetodolist.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.emindev.expensetodolist.BuildConfig
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.main.data.sharedPreference.PageSettings
import com.emindev.expensetodolist.databinding.ActivityMainBinding
import com.emindev.expensetodolist.expense.ui.fragment.FragmentExpense
import com.emindev.expensetodolist.helperlibrary.common.helper.SystemInfo
import com.emindev.expensetodolist.helperlibrary.common.helper.addLog
import com.emindev.expensetodolist.helperlibrary.common.model.Resource
import com.emindev.expensetodolist.helperlibrary.ui.alertDialogClassic
import com.emindev.expensetodolist.income.ui.fragment.FragmentIncome
import com.emindev.expensetodolist.main.common.util.RemoteData
import com.emindev.expensetodolist.main.data.viewmodel.MainViewModel
import com.emindev.expensetodolist.main.ui.fragment.FragmentMain
import com.emindev.expensetodolist.main.data.update.RemoteRepository
import com.emindev.expensetodolist.main.data.update.RemoteSettings


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }
        }


        SystemInfo.PACKAGE_NAME = packageName

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationBar()
        binding.bottomNavigation.selectedItemId = R.id.itemHome
        setFragmentMain()
        mainViewModel._isLoading.value = false

        updateCheck()
    }


    private fun setFragmentIncome() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentIncome(this, this, binding.fabAdd)).commit()

    }

    private fun setFragmentMain() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentMain(this)).commit()
    }

    private fun setFragmentExpense() {
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, FragmentExpense(this, this, binding.fabAdd)).commit()
    }

    private fun setNavigationBar() {
        binding.bottomNavigation.setOnItemSelectedListener { selectedItem ->
            PageSettings(this).setPageLocation(selectedItem.itemId)
            when (selectedItem.itemId) {
                R.id.itemIncome -> {
                    setFragmentIncome()
                    binding.fabAdd.show()
                    true
                }

                R.id.itemHome -> {
                    setFragmentMain()
                    binding.fabAdd.hide()
                    true
                }

                R.id.itemExpense -> {
                    setFragmentExpense()
                    binding.fabAdd.show()
                    true
                }

                else -> false
            }
        }
    }

    private fun updateCheck() {

        fun remoteDialog(remote: RemoteData) {
            if (remote.isLocked)
                alertDialogClassic(getString(R.string.locked), getString(R.string.locked_content), false)
            else if (remote.hasUpdate)
                if (remote.forceUpdate)
                    alertDialogClassic(getString(R.string.update), getString(R.string.update_content), false)
                else
                    alertDialogClassic(getString(R.string.update), getString(R.string.update_force_content), getString(R.string.ok), {})
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
