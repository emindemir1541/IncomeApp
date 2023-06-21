package com.emindev.expensetodolist.main.data.retrofit

import com.emindev.expensetodolist.main.common.model.CardModel
import com.emindev.expensetodolist.main.common.util.ColorUtil

data class CurrencyModel(
    val buying: String,
    val name: String,
    val selling: String
){
    val cardModel: CardModel
        get() =CardModel(title = name, listOf(buying,selling), cardColor = ColorUtil.randomColor,)

}