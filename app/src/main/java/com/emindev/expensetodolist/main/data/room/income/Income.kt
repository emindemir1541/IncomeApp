package com.emindev.expensetodolist.main.data.room.income

import android.content.Context
import androidx.room.Entity
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.Companion.isMonthAndYearEqualTo
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import java.time.LocalDate

@Entity
data class Income(
    val id:Long,
    val cardId:Long,
    val name:String,
    val amount:Float,
    val initialDate:String,
    val currentDate:String,
    val deleted:Boolean,
    val isRepeatable: Boolean,
){
    fun remainingDay(mContext: Context): String = if (isMoneyPaid) mContext.getString(R.string.paid) else (DateUtil.dayBetweenTwoDate(currentLocalDate, DateUtil.localDateTimeNow.toLocalDate())).toString() +" "+ mContext.getString(R.string.day_remained)

    val toIncomeModel:IncomeModel
        get() = IncomeModel(name, amount,initialDate,deleted,isRepeatable,id)

    val toIncomeCardModel:IncomeCardModel
        get() = IncomeCardModel(id,currentDate,amount,cardId)

    val initialLocalDate:LocalDate
        get()= SqlDateUtil.convertDate(initialDate)

    val currentLocalDate:LocalDate
        get()= SqlDateUtil.convertDate(currentDate)

    val isMoneyPaid:Boolean
        get() = currentLocalDate.dayOfMonth <= DateUtil.localDateTimeNow.dayOfMonth && currentLocalDate.isMonthAndYearEqualTo(DateUtil.localDateNow)






}