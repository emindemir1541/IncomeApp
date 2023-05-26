package com.emindev.expensetodolist.main.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.income.common.currentDate
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "table_income")
@Parcelize
data class Income(
    val name: String,
    val amount: Float,
    val _initialDate: String,
    var _currentDate:String,
    var deleted: Boolean,
    var isRepeatable: Boolean,
    var cardId:Long= DateUtil.currentTime,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable {}

