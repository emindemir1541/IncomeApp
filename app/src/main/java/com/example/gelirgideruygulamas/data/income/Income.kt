package com.example.gelirgideruygulamas.data.income

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.helper.DateHelper
import com.example.gelirgideruygulamas.helper.DateHelper.Companion.checkMonthAndYear
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Entity(tableName = "table_income")
@Parcelize
data class Income(
    val name: String?,
    val amount: Float?,
    val savedDate: Long,
    var date: Long,
    var deleted: Boolean,
    var dataChanged: Long = DateHelper.currentTime, //livedatanın çalışması için datayı değiştiriyor
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var savedLocalDateTime = DateHelper.convertToDateTime(savedDate)

    @IgnoredOnParcel
    @Ignore
    var localDateTime = DateHelper.convertToDateTime(date)

    fun isSelected(context: Context): Boolean {
        //cart StatedDate de kaydedilen tarihle uyuşuyor mu
        return  StatedDate(context).getDateTime().checkMonthAndYear(savedLocalDateTime)
    }
}

