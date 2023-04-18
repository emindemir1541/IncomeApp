package com.example.gelirgideruygulamas.main.data.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel():ViewModel() {
     val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()



}