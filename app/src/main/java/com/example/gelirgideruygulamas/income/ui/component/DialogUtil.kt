package com.example.gelirgideruygulamas.income.ui.component

import android.app.Dialog
import android.content.Context
import com.example.gelirgideruygulamas.helperlibrary.common.helper.Helper.isDarkThemeOn


object DialogUtil {
    fun materialThemeDialog(context: Context): Dialog {
        return if (context.isDarkThemeOn) {
            Dialog(context, android.R.style.Theme_Material_NoActionBar)
        }
        else {
            Dialog(context, android.R.style.Theme_Material_Light_NoActionBar)
        }
    }
}

