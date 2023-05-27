package com.emindev.expensetodolist.main.data.room.income

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_income_card")
@Parcelize
data class IncomeCardModel(
    val id: Long,
    val currentDate:String,
    val amount:Float,
    @PrimaryKey(autoGenerate = true)
    val cardId:Long = 0
):Parcelable