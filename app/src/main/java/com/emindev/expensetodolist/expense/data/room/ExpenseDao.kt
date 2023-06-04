package com.emindev.expensetodolist.expense.data.room

import androidx.room.*
import com.emindev.expensetodolist.income.data.room.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsert(expenseModel: ExpenseModel)

    @Upsert
    suspend fun upsert(expenseCardModel: ExpenseCardModel)

/*    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType = 'ONCE' and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithOneCardBySelectedDate(month: String,year: String,delimiter:String): Flow<List<Expense>>

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType IN ('INFINITY', 'LIMITED') and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithMultipleCardBySelectedDate(month: String,year: String,delimiter:String): Flow<List<Expense>>*/
}
