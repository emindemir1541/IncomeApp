package com.example.gelirgideruygulamas.income.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil
import com.example.gelirgideruygulamas.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Entity(tableName = "table_income")
@Parcelize
data class Income(
    val name: String?,
    val amount: Float,
    val startedDateLong: Long,
    var day:Int,
    var month:Int,
    var year:Int,
    var deleted: Boolean,
    var isRepeatable: Boolean,
    var cardId:Long= DateUtil.currentTime,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var startedDate: LocalDate = DateUtil.convertToDateTime(startedDateLong).toLocalDate()

    @IgnoredOnParcel
    @Ignore
    var date: LocalDate = DateUtil.convertToDateTime(year,month,day).toLocalDate()



    //para yattı mı
    @IgnoredOnParcel
    @Ignore
    val itsTime = date.dayOfMonth <= DateUtil.currentDateTime.dayOfMonth && date.checkMonthAndYear(DateUtil.currentDateTime)


}

