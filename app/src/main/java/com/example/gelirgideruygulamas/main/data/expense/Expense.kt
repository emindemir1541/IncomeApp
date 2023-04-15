package com.example.gelirgideruygulamas.main.data.expense

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.example.gelirgideruygulamas.main.common.constant.ExpenseCardSituation
import com.example.gelirgideruygulamas.main.common.constant.ExpenseType
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import java.time.LocalDate

@Entity(tableName = "table_expense")
@Parcelize
data class Expense(
    var name: String,
    var amount: Float,
    var startedDateLong: Long,
    var completed: Boolean,
    var debt: Boolean,
    var lender: String?,
    var repetition: Int?,
    var deleted: Boolean,
    var type: ExpenseType,
    var dateLong: Long,
    var dataChanged: Long = DateUtil.currentTime, //livedatanın çalışması için datayı değiştiriyor
    var cardId: Long = DateUtil.currentTime,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var startedDate: LocalDate = DateUtil.convertToDateTime(startedDateLong).toLocalDate()

    @IgnoredOnParcel
    @Ignore
    var date: LocalDate = DateUtil.convertToDateTime(dateLong).toLocalDate()

    @Ignore
    @IgnoredOnParcel
    val itsTime = (date.dayOfMonth <= DateUtil.currentDateTime.dayOfMonth && date.checkMonthAndYear(DateUtil.currentDateTime)) || completed




}





/*    fun getCard(cardId: Int): ExpenseCardTable {
        return monthlyCard.single { it.cardId == cardId }
    }

    fun getCard(dateLong: Date): ExpenseCardTable {
        return monthlyCard.single { it.month == dateLong.month && it.year == dateLong.year }
    }*/