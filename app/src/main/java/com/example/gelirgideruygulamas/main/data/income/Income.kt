package com.example.gelirgideruygulamas.main.data.income

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.main.data.sharedPreference.StatedDate
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate

@Entity(tableName = "table_income")
@Parcelize
data class Income(
    val name: String?,
    val amount: Float,
    val startedDateLong: Long,
    var dateLong: Long,
    var deleted: Boolean,
    var repetation: Boolean,
    var dataChanged: Long = DateUtil.currentTime, //livedatanın çalışması için datayı değiştiriyor
    var cardId:Long= DateUtil.currentTime,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var startedDate: LocalDate = DateUtil.convertToDateTime(startedDateLong).toLocalDate()

    @IgnoredOnParcel
    @Ignore
    var date: LocalDate = DateUtil.convertToDateTime(dateLong).toLocalDate()

    fun remainingDay(mContext:Context): String = if (itsTime) mContext.getString(R.string.paid) else (DateUtil.dayBetweenTwoDate(date, DateUtil.currentDateTime.toLocalDate())).toString() +" "+ mContext.getString(R.string.day_remained)


    //para yattı mı
    @IgnoredOnParcel
    @Ignore
    val itsTime = date.dayOfMonth <= DateUtil.currentDateTime.dayOfMonth && date.checkMonthAndYear(DateUtil.currentDateTime)


    fun isSelected(context: Context): Boolean {
        //cart StatedDate de kaydedilen tarihle uyuşuyor mu
        return StatedDate(context).dateTime.checkMonthAndYear(date)
    }

  /*  fun createIncome(incomeList:List<Income>){
        if (!incomeList[incomeList.size-1].getDate.checkMonthAndYear(getDate))
            DateHelper
    }*/
}

