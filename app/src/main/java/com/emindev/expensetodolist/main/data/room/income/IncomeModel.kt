package com.emindev.expensetodolist.main.data.room.income

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_income")
@Parcelize
data class IncomeModel(
    val name: String,
    val latestAmount: Float,
    val initialDate: String,
    var deleted: Boolean,
    var isRepeatable: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) : Parcelable

