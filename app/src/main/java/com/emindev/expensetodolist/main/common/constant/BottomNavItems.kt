package com.emindev.expensetodolist.main.common.constant

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.ui.graphics.vector.ImageVector
import com.emindev.expensetodolist.R

sealed class BottomNavItems(val name: Int,val route: String,val icon: ImageVector){
    object IncomePage:BottomNavItems(R.string.income,"incomePage", Icons.Rounded.Phone)
    object MainPage:BottomNavItems(R.string.main_page,"mainPage", Icons.Rounded.Home)
    object ExpensePage:BottomNavItems(R.string.expense,"expensePage", Icons.Rounded.Email)

}
