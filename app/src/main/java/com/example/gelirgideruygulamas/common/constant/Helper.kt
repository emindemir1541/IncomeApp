package com.example.gelirgideruygulamas.common.constant

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES

class Helper {

    companion object {

        fun isDarkThemeOn(mContext: Context): Boolean {
            return mContext.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
        }

        fun Float.clearZero(): String {
            val splitValue = this.toString().split(".")
            return if (splitValue[1] == "0") {
                splitValue[0]
            }
            else this.toString()
        }
    }

}