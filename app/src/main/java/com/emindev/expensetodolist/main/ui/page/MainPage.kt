package com.emindev.expensetodolist.main.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emindev.expensetodolist.main.common.constant.Currency
import com.emindev.expensetodolist.main.common.model.CardModel
import com.emindev.expensetodolist.main.common.model.FinanceModel
import com.emindev.expensetodolist.main.common.model.Resource
import com.emindev.expensetodolist.main.data.retrofit.CurrencyViewModel
import com.emindev.expensetodolist.main.data.viewmodel.FinanceViewModel
import com.emindev.expensetodolist.main.ui.component.ProgressBarRemainedMoney

@Composable
fun MainPage(financeViewModel: FinanceViewModel, currencyViewModel: CurrencyViewModel) {

    val context = LocalContext.current

    val finance = financeViewModel.finance.collectAsState(initial = FinanceModel(0f, 0f, 0f, 0f, 0f, 0f, 0f))

    val currencyModelList = currencyViewModel.currency.collectAsState()


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp, top = 32.dp), verticalArrangement = Arrangement.spacedBy(40.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            ProgressBarRemainedMoney(finance = finance, size = 100.dp)
        }
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalItemSpacing = 16.dp) {


            items(finance.value.getCardModelList(context)) { finance ->
                FinanceCard(financeCardModel = finance)
            }


            when (currencyModelList.value) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {

                    items(currencyModelList.value.data!!) { currency ->
                        FinanceCard(currency.cardModel)
                    }
                }
            }


            item {
                Box(modifier = Modifier.height(160.dp))
            }
            item {
                Box(modifier = Modifier.height(160.dp))
            }
        }
    }
}

@Composable
private fun FinanceCard(financeCardModel: CardModel) {
    Card(modifier = Modifier, colors = CardDefaults.cardColors(containerColor = financeCardModel.cardColor), border = BorderStroke(2.dp,financeCardModel.cardColor)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (financeCardModel.painterId != null) {
                Image(painter = painterResource(id = financeCardModel.painterId), contentDescription ="Painter" ,Modifier.size(70.dp))
            }
            if (financeCardModel.imageVector != null) {
                Icon(imageVector = financeCardModel.imageVector, contentDescription = "ImageVector")
            }
            Text(text = financeCardModel.title, fontSize = 20.sp, textAlign = TextAlign.Center)

            financeCardModel.subTitleList.forEach { subTitle ->
                Text(text = subTitle + Currency.TL)
            }

        }
    }
}

