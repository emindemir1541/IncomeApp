package com.emindev.expensetodolist.expense.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "table_expense")
@Parcelize
data class ExpenseModel(
    val name: String,
    val latestAmount: Float,
    val initialDate: String,
    val deleted: Boolean,
    val repeatType:RepeatType,
    val repetition:Int?,
    val expenseType:ExpenseType,
    val lender:String?,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) : Parcelable {

    val initialLocalDate: LocalDate
        get() = SqlDateUtil.convertDate(initialDate)

}