package com.emindev.expensetodolist.expense.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "table_expense_card")
data class ExpenseCardModel(
    val currentAmount: Float,
    val currentDate: String,
    val cardDeleted: Boolean,
    val completed: Boolean,
    val id: Long,
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0
) : Parcelable {
    val currentLocalDate: LocalDate
        get()= SqlDateUtil.convertDate(currentDate)
}
