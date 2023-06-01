package com.emindev.expensetodolist.main.data.room.income

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "table_income")
@Parcelize
data class IncomeModel(
    val name: String,
    val latestAmount: Float,
    val initialDate: String,
    var deleted: Boolean,
    var repeatType: RepeatType,
    var repetition:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) : Parcelable {
    val initialLocalDate: LocalDate
        get()= SqlDateUtil.convertDate(initialDate)


}

