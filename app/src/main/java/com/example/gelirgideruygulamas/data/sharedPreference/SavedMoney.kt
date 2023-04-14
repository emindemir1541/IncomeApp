package com.example.gelirgideruygulamas.data.sharedPreference

import android.content.Context
import com.example.gelirgideruygulamas.common.constant.DateUtil
import com.example.gelirgideruygulamas.common.constant.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.common.constant.DateUtil.toLong
import java.time.LocalDateTime

private const val SAVED_MONEY = "SAVED_MONEY"
private const val SAVED_MONEY_TEMPORARY = "SAVED_MONEY_TEMPORARY"
private const val SAVED_MONEY_PERMANENT = "SAVED_MONEY_PERMANENT"
private const val SAVED_MONEY_DATE = "SAVED_MONEY_DATE"

class SavedMoney(private val mContext: Context) {

    private val repository = SharedPreferenceRepository(mContext)

    fun setTemporary(money: Float, date: LocalDateTime) {
        repository.setLong(SAVED_MONEY, SAVED_MONEY_DATE, date.toLong())
        repository.setFloat(SAVED_MONEY, SAVED_MONEY_TEMPORARY, money)
        // TODO: iki kayıt yapmıyor
    }

    fun setPermanent(money: Float) {
        repository.setFloat(SAVED_MONEY, SAVED_MONEY_PERMANENT, money)
    }

    fun getTemporary() = repository.getFloat(SAVED_MONEY, SAVED_MONEY_TEMPORARY)

    fun getPermanent()=repository.getFloat(SAVED_MONEY, SAVED_MONEY_PERMANENT)

    fun getDate() = DateUtil.convertToDateTime(repository.getLong(SAVED_MONEY, SAVED_MONEY_DATE))

    fun resetTemporary()= setTemporary(0f, DateUtil.currentDateTime)

    fun checkDate() = !getDate().checkMonthAndYear(DateUtil.currentDateTime)

    fun resetPermanent() = setPermanent(0f)

    fun isPermanentEmpty() = getPermanent() == 0f

    fun transferCheck() {
        if (checkDate()) {
            setPermanent(getTemporary())
            resetTemporary()
        }
    }

}