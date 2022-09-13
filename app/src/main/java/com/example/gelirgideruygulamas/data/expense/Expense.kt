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
    var payDay: Long,
    var dataChanged: Long = DateHelper.currentTime, //livedatanın çalışması için datayı değiştiriyor
    var cardId: Int = 1,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var startedDate = DateHelper.convertToDateTime(startedDateLong)


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

/*    fun isSelected(context: Context): Boolean {
        //cart, StatedDate de kaydedilen tarihle uyuşuyor mu
        return StatedDate(context).getDateTime().checkMonthAndYear(localDateTime)
    }*/

}


enum class ExpenseCardType {
    DONE,
    UNDONE,
    ONCE
}


/*    fun getCard(cardId: Int): ExpenseCardTable {
        return monthlyCard.single { it.cardId == cardId }
    }

    fun getCard(payDay: Date): ExpenseCardTable {
        return monthlyCard.single { it.month == payDay.month && it.year == payDay.year }
    }*/