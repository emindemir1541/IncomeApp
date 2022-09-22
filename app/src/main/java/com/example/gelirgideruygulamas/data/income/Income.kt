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
import java.time.LocalDate

@Entity(tableName = "table_income")
@Parcelize
data class Income(
    val name: String?,
    val amount: Float,
    val savedDateLong: Long,
    var dateLong: Long,
    var deleted: Boolean,
    var dataChanged: Long = DateHelper().currentTime, //livedatanın çalışması için datayı değiştiriyor
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var savedDate: LocalDate = DateHelper.convertToDateTime(savedDateLong).toLocalDate()

    @IgnoredOnParcel
    @Ignore
    var date: LocalDate = DateHelper.convertToDateTime(dateLong).toLocalDate()

    fun isSelected(context: Context): Boolean {
        //cart StatedDate de kaydedilen tarihle uyuşuyor mu
        return  StatedDate(context).getDateTime().checkMonthAndYear(savedDate)
    }
}

