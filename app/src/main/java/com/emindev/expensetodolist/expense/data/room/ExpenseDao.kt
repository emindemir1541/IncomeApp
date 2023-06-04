package com.emindev.expensetodolist.expense.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsert(expenseModel: ExpenseModel):Long
    @Upsert
    suspend fun upsert(expenseCardModel: ExpenseCardModel):Long

    @Query("update table_expense_card set currentAmount = :amount where id = :id and date(currentDate) >= date(:dateForFilter)")
    suspend fun updateAmountOfCardsAfterSpecificDate(id:Long,dateForFilter:String,amount:Float)

    @Query("select table_expense.id,table_expense_card.cardId,table_expense.name,table_expense.latestAmount,table_expense_card.currentAmount,table_expense.initialDate,table_expense_card.currentDate ,table_expense_card.completed,table_expense.deleted,table_expense_card.cardDeleted,table_expense.repeatType, table_expense.repetition,table_expense.expenseType,table_expense.lender" +
            " from table_expense left join table_expense_card on table_expense.id = table_expense_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType = 'ONCE' and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithOneCardBySelectedDate(month: String,year: String,delimiter:String): Flow<List<Expense>>

    @Query("select table_expense.id,table_expense_card.cardId,table_expense.name,table_expense.latestAmount,table_expense_card.currentAmount,table_expense.initialDate,table_expense_card.currentDate ,table_expense_card.completed,table_expense.deleted,table_expense_card.cardDeleted,table_expense.repeatType, table_expense.repetition,table_expense.expenseType,table_expense.lender" +
            " from table_expense left join table_expense_card on table_expense.id = table_expense_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType IN ('INFINITY', 'LIMITED') and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithMultipleCardBySelectedDate(month: String,year: String,delimiter:String): Flow<List<Expense>>
}
