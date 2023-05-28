package com.emindev.expensetodolist.main.data.viewmodel

import androidx.lifecycle.ViewModel
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime

class MainViewModel() : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtil.localDateNow)
    val selectedDate = _selectedDate.asStateFlow()
    fun setSelectedDate(selectedDate: LocalDate) {
        _selectedDate.value = selectedDate
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


}