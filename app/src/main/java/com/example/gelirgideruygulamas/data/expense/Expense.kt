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
    var savedDateTime: Long,
    var done: Boolean,
    var debt: Boolean,
    var lender: String?, //null ise borç veren yok
    var repetition: Int?,  // her ay için 0, tekrarsız için null, kalan tekrar sayısı
    var deleted: Boolean,
    var type: Int, //need, want, debt
    var dateTime: Long,
    var dataChanged: Long = DateHelper.currentTime, //livedatanın çalışması için datayı değiştiriyor
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var localDateTime: LocalDateTime = DateHelper.convertToDateTime(dateTime)

    @Ignore
    @IgnoredOnParcel
    var savedLocalDateTime: LocalDateTime = DateHelper.convertToDateTime(savedDateTime)

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
            done -> {
                ExpenseCardType.DONE
            }
            else -> ExpenseCardType.UNDONE
        }
    }

    fun isSelected(context: Context): Boolean {
        //cart, StatedDate de kaydedilen tarihle uyuşuyor mu
        return StatedDate(context).getDateTime().checkMonthAndYear(localDateTime)
    }

}



enum class ExpenseCardType {
    DONE,
    UNDONE,
    ONCE
}

@Entity
@Parcelize
data class SavedDate(var savedDateTime: Long) : Parcelable{

    @Ignore
    @IgnoredOnParcel
    val localDateTime = DateHelper.convertToDateTime(savedDateTime)
}

/*    fun getCard(cardId: Int): ExpenseCardTable {
        return monthlyCard.single { it.cardId == cardId }
    }

    fun getCard(date: Date): ExpenseCardTable {
        return monthlyCard.single { it.month == date.month && it.year == date.year }
    }*/