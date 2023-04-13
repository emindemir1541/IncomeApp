package com.example.gelirgideruygulamas.data.expense

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate
import com.example.gelirgideruygulamas.common.helper.DateUtil
import com.example.gelirgideruygulamas.common.helper.DateUtil.Companion.checkMonthAndYear
import com.example.gelirgideruygulamas.common.helper.DateUtil.Companion.convertToDateTime
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
    var lender: String?, //null ise borç veren yok
    var repetition: Int?,  //tekrarsız için null, kalan tekrar sayısı
    var deleted: Boolean,
    var type: Int, //need, want, debt
    var dateLong: Long,
    var dataChanged: Long = DateUtil().currentTime, //livedatanın çalışması için datayı değiştiriyor
    var cardId: Long = DateUtil().currentTime,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var startedDate: LocalDate = convertToDateTime(startedDateLong).toLocalDate()

    @IgnoredOnParcel
    @Ignore
    var date: LocalDate = convertToDateTime(dateLong).toLocalDate()

    companion object {
        //Expense Type
        const val NEED = 0
        const val WANT = 1
        const val DEBT = 2
    }

    fun remainingDay(mContext: Context): String {
        return if (completed) {
            mContext.getString(R.string.expense_paid)
        }
        else if(itsTime) {
            mContext.getString(R.string.payment_time)
        }
        else {
            (DateUtil.dayBetweenTwoDate(date, DateUtil().currentDateTime.toLocalDate())).toString() + " " + mContext.getString(R.string.day_remained)
        }
    }

    fun getCardType(): ExpenseCardType {
        return when {
            repetition == null -> {
                ExpenseCardType.ONCE
            }
            completed -> {
                ExpenseCardType.DONE
            }
            else -> ExpenseCardType.UNDONE
        }
    }

    @Ignore
    @IgnoredOnParcel
    val itsTime = (date.dayOfMonth <= DateUtil().currentDateTime.dayOfMonth && date.checkMonthAndYear(DateUtil().currentDateTime))  || completed


    fun isSelected(context: Context): Boolean {
        //cart, StatedDate de kaydedilen tarihle uyuşuyor mu
        return StatedDate(context).getDateTime().checkMonthAndYear(date)
    }

}


enum class ExpenseCardType {
    DONE,
    UNDONE,
    ONCE
}


/*    fun getCard(cardId: Int): ExpenseCardTable {
        return monthlyCard.single { it.cardId == cardId }
    }

    fun getCard(dateLong: Date): ExpenseCardTable {
        return monthlyCard.single { it.month == dateLong.month && it.year == dateLong.year }
    }*/