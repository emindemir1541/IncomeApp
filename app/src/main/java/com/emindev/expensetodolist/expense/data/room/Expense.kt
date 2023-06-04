package com.emindev.expensetodolist.expense.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity
data class Expense(
    val id:Long,
    val cardId:Long,
    val name:String,
    val latestAmount:Float,
    val currentAmount:Float,
    val initialDate: String,
    val currentDate:String,
    val completed:Boolean?,
    val deleted:Boolean,
    val cardDeleted:Boolean,
    val repeatType: RepeatType,
    val repetition:Int?,
    val expenseType: ExpenseType,
    val lender:String,
){
    val initialLocalDate:LocalDate
        get() = SqlDateUtil.convertDate(initialDate)

    val currentLocalDate:LocalDate
        get() = SqlDateUtil.convertDate(currentDate)


}
