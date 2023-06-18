package com.emindev.expensetodolist.main.data.viewmodel

import androidx.lifecycle.ViewModel
import com.emindev.expensetodolist.expense.data.room.ExpenseViewModel
import com.emindev.expensetodolist.income.data.room.IncomeViewModel
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.constant.BottomNavItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class MainViewModel : ViewModel() {


    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtil.localDateNow)
    val selectedDate = _selectedDate.asStateFlow()
    fun setSelectedDate(selectedDate: LocalDate) {
        _selectedDate.value = selectedDate
    }

    private val _bottomNavItem = MutableStateFlow<BottomNavItems>(BottomNavItems.MainPage)
    val bottomNavItem = _bottomNavItem.asStateFlow()

    private val _isCardCreating = MutableStateFlow(false)
    val isCardCreating = _isCardCreating.asStateFlow()

    private val _jobInteraction = MutableStateFlow(false)
    val jobInteraction = _jobInteraction.asStateFlow()



    val jobInteractionStarted:Unit
        get(){
            _jobInteraction.value = true
        }

    val jobInteractionFinished:Unit
        get() {
            _jobInteraction.value = false
        }
    fun setNavItem(bottomNavItem: BottomNavItems) {
        _bottomNavItem.value = bottomNavItem
    }

    val cardCreatingStarted:Unit
        get() {
            _isCardCreating.value = true
        }

    val cardCreatingFinished:Unit
        get(){
            _isCardCreating.value = false
        }

    val appIsLoading: Unit
        get() {
            _isLoading.value = true
        }

    val appLoaded: Unit
        get() {
            _isLoading.value = false
        }
    val nextMonth: Unit
        get() {
            _selectedDate.update {
                it.plusMonths(1)
            }
        }

    val previousMonth: Unit
        get() {
            _selectedDate.update {
                it.minusMonths(1)
            }
        }

    fun selectCurrentDate() {
        _selectedDate.value = DateUtil.localDateNow
    }

}
