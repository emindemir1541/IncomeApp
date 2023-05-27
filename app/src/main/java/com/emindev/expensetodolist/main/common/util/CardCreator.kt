package com.emindev.expensetodolist.main.common.util

object CardCreator {

    //infinity income card creator
   /* fun incomeCreator(incomeViewModel: IncomeViewModel) {
        CoroutineScope(Dispatchers.IO).launch {
            // TODO: check if this month already created, you can save this settings to datastore app settings
            // TODO: if eğer başlanbıç tarihi şuanki tarihten ilerideyse onu dahil etme 
            // TODO: ileride gösterilmesi gereken kartlar için yansıma kullan

            var incomeList = emptyList<Income>()
            var uniqueCardIdList = emptyList<Income>()

            var incomeListLoaded = false
            var uniqueCardIdListLoaded = false

            incomeViewModel.allIncomes.collectLatest { _incomeList ->
                incomeList = _incomeList
                test = "incomeList:     $incomeList"
                incomeListLoaded = true
            }
            incomeViewModel.uniqueCardIds.collectLatest { _uniqueCardIdList ->
                uniqueCardIdList = _uniqueCardIdList
                test = "uniquecardlist:     $uniqueCardIdList"
                uniqueCardIdListLoaded = true
            }

            while (!(incomeListLoaded&&uniqueCardIdListLoaded)){
                test = "waiting for list load"
                delay(200)
            }



            incomeList.filter { income: Income -> income.isRepeatable }

            test = "repeatable incomes:     $incomeList"

            for (cardId in uniqueCardIdList) {
                val currentIncomeList = incomeList
                currentIncomeList.filter { income: Income -> income.cardId == cardId.cardId }
                test = "currentIncome cards:    $currentIncomeList"
                val incomeReference=currentIncomeList.first()
                test = "reference income:      $incomeReference"
                val dateCounter = incomeReference.initialDate

                while (dateCounter == DateUtil.currentDateTime.toLocalDate()) {
                    dateCounter.plusMonths(1)
                    val incomeCard = currentIncomeList.find { income: Income -> income.currentDate == dateCounter }
                    test = "does income card exist in this date: $dateCounter"
                    test = incomeCard
                    if (incomeCard == null) {
                        incomeViewModel.addIncomeCard(incomeReference,dateCounter)
                        test = "added new card"
                    }

                }

            }

        }

    }*/
}