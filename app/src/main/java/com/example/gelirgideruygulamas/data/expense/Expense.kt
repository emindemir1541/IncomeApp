package com.example.gelirgideruygulamas.data.expense

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
import java.time.LocalDateTime

@Entity(tableName = "table_expense")
@Parcelize
data class Expense(
    var name: String,
    var amount: Float,
    var startedDateLong: Long,
    var completed: Boolean,
    var debt: Boolean,
    var lender: String?, //null ise borç veren yok
    var repetition: Int?,  // her ay için 0, tekrarsız için null, kalan tekrar sayısı
    var deleted: Boolean,
    var type: Int, //need, want, debt
    var dateLong: Long,
    var dataChanged: Long = DateHelper.currentTime, //livedatanın çalışması için datayı değiştiriyor
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cardId:String="card_$id",
    ) : Parcelable {



    @IgnoredOnParcel
    @Ignore
    var startedDate:LocalDate = DateHelper.convertToDateTime(startedDateLong).toLocalDate()

    @IgnoredOnParcel
    @Ignore
    var date:LocalDate = DateHelper.convertToDateTime(dateLong).toLocalDate()

    companion object {
        //Expense Type
        const val NEED = 0
        const val WANT = 1
        const val DEBT = 2
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