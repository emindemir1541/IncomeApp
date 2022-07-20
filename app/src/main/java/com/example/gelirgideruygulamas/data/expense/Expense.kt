package com.example.gelirgideruygulamas.data.expense

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gelirgideruygulamas.data.expense.ExpenseCardType.*
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "table_expense")
@Parcelize
data class Expense(
    val name: String,
    val amount: Float,
    val savedDate: String,
    val date: String,
    val done: Boolean,
    val debt: Boolean,
    val lender: String?,
    val repetition: Int?,  // her ay için 0, tekrarsız için null, kalan tekrar sayısı
    //val type: ExpenseType,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Parcelable {

    fun getCardType():ExpenseCardType {
        return when {
            repetition == null -> {
                 ONCE
            }
            done -> {
                DONE
            }
            else -> UNDONE
        }
    }
}

