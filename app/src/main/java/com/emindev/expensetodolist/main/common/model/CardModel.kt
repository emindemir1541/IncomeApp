package com.emindev.expensetodolist.main.common.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CardModel(
    val title:String,
    val subTitleList: List<String>,
    val cardColor:Color,
    val imageVector: ImageVector? =null,
    val painterId:Int? = null,
)
