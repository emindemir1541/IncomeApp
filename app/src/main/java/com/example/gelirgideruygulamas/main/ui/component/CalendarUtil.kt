package com.example.gelirgideruygulamas.main.ui.component

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

object CalendarUtil {
    fun <S> MaterialDatePicker.Builder<S>.setMinDate(minDate:Long): MaterialDatePicker.Builder<S> {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(minDate))
            .build()
        this.setCalendarConstraints(constraints)
        return this
    }
    fun <S> MaterialDatePicker.Builder<S>.setMinDateNow(): MaterialDatePicker.Builder<S> {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()
        this.setCalendarConstraints(constraints)
        return this
    }

}