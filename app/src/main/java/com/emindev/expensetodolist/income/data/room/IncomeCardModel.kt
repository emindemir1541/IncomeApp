package com.emindev.expensetodolist.income.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "table_income_card")
@Parcelize
data class IncomeCardModel(
    val id: Long,
    val currentDate: String,
    val cardAmount: Float,
    val cardDeleted:Boolean,
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0
) : Parcelable {
    val currentLocalDate: LocalDate
        get()= SqlDateUtil.convertDate(currentDate)
}