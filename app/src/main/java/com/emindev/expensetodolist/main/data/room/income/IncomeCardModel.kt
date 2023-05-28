package com.emindev.expensetodolist.main.data.room.income

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "table_income_card")
@Parcelize
data class IncomeCardModel(
    val id: Long,
    val currentDate: String,
    val amount: Float,
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0
) : Parcelable {
    val currentLocalDate: LocalDate
        get()= SqlDateUtil.convertDate(currentDate)
}