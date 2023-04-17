package com.example.gelirgideruygulamas.main.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.toLong
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefFileNames
import com.example.gelirgideruygulamas.main.common.constant.SharedPrefSavedMoney
import java.time.LocalDateTime



class SavedMoney(context: Context) {

    private val repository = SharedPreferenceRepository(context)

    fun setTemporary(money: Float, date: LocalDateTime) {
        repository.setLong(SharedPrefFileNames.SAVED_MONEY, SharedPrefSavedMoney.SAVED_MONEY_DATE, date.toLong())
        repository.setFloat(SharedPrefFileNames.SAVED_MONEY, SharedPrefSavedMoney.SAVED_MONEY_TEMPORARY, money)
        // TODO: iki kayıt yapmıyor
    }

    fun setPermanent(money: Float) {
        repository.setFloat(SharedPrefFileNames.SAVED_MONEY, SharedPrefSavedMoney.SAVED_MONEY_PERMANENT, money)
    }

    val temporaryMoney get() = repository.getFloat(SharedPrefFileNames.SAVED_MONEY, SharedPrefSavedMoney.SAVED_MONEY_TEMPORARY)


    //That is the money which remained from last month
    val permanentMoney get() = repository.getFloat(SharedPrefFileNames.SAVED_MONEY, SharedPrefSavedMoney.SAVED_MONEY_PERMANENT)

    val savedDate get() = DateUtil.convertToDateTime(repository.getLong(SharedPrefFileNames.SAVED_MONEY, SharedPrefSavedMoney.SAVED_MONEY_DATE))

    val resetTemporary get() = setTemporary(0f, DateUtil.currentDateTime)

    val checkDate get() = !savedDate.checkMonthAndYear(DateUtil.currentDateTime)

    fun resetPermanent() = setPermanent(0f)

    val isPermanentEmpty get() = permanentMoney == 0f

    fun transferCheck() {
        if (checkDate) {
            setPermanent(temporaryMoney)
            resetTemporary
        }
    }

}