package com.emindev.expensetodolist.income.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Upsert
    suspend fun upsert(incomeModel: IncomeModel): Long

    @Upsert
    suspend fun upsert(incomeCardModel: IncomeCardModel): Long

    @Query("update table_income_card set cardAmount = :amount where id = :id and date(currentDate) >= date(:dateForFilter)")
    suspend fun updateAmountOfCardsAfterSpecificDate(id: Long, dateForFilter: String, amount: Float)

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType = 'ONCE' and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithOneCardBySelectedDate(month: String, year: String, delimiter: String): Flow<List<Income>>

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType IN ('INFINITY', 'LIMITED') and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithMultipleCardBySelectedDate(month: String, year: String, delimiter: String): Flow<List<Income>>

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0  and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getAllIncomesNotDeletedBySelectedDate(month: String, year: String, delimiter: String): Flow<List<Income>>

    @Query("select * from table_income where repeatType = 'INFINITY' and deleted = 0 order by initialDate")
    fun getInfinityIncomeModelsNotDeleted(): Flow<List<IncomeModel>>


    @Query("select * from table_income_card order by currentDate")
    fun getIncomeCardModels(): Flow<List<IncomeCardModel>>

}