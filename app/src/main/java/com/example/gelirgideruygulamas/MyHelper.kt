package com.example.gelirgideruygulamas

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES

class MyHelper {
    companion object {

        fun isDarkThemeOn(mContext:Context): Boolean {
            return mContext.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
        }

    }

}