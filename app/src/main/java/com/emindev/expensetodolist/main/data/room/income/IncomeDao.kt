package com.emindev.expensetodolist.main.data.room.income

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Upsert
    suspend fun upsert(incomeModel: IncomeModel):Long

    @Upsert
    suspend fun upsert(incomeCardModel: IncomeCardModel):Long

    @Query("update table_income_card set cardAmount = :amount where id = :id and date(currentDate) >= date(:dateForFilter)")
    suspend fun updateAmountOfCardsAfterSpecificDate(id:Long,dateForFilter:String,amount:Float)
/*
    @Delete
    suspend fun delete(income: Income)
*/

    @Query("Delete from table_income")
    suspend fun deleteAll()

   @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition " +
           " from table_income left join table_income_card on table_income.id = table_income_card.id " +
           "where deleted = 0 and cardDeleted = 0 order by cardId")
    fun readAllData(): Flow<List<Income>>

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType = 'ONCE' and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithOneCardBySelectedDate(month: String,year: String,delimiter:String): Flow<List<Income>>

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.cardAmount,table_income.latestAmount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income_card.cardDeleted,table_income.repeatType, table_income.repetition" +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and cardDeleted = 0 and repeatType IN ('INFINITY', 'LIMITED') and currentDate like :year ||  :delimiter || :month ||'%' order by currentDate")
    fun getIncomesWithMultipleCardBySelectedDate(month: String,year: String,delimiter:String): Flow<List<Income>>
    @Query("select * from table_income where deleted = 0 order by initialDate")
    fun getIncomeModelsNotDeleted():Flow<List<IncomeModel>>
    @Query("select * from table_income_card where cardDeleted = 0 order by currentDate")
    fun getIncomeCardModelsNotDeleted():Flow<List<IncomeCardModel>>

    @Query("select * from table_income order by initialDate")
    fun getIncomeModels():Flow<List<IncomeModel>>
    @Query("select * from table_income_card order by currentDate")
    fun getIncomeCardModels():Flow<List<IncomeCardModel>>
    @Query("select * from table_income_card where id =:incomeId order by currentDate")
    fun getCardsByIncome(incomeId:Long):Flow<List<IncomeCardModel>>




/*
    @Query("select * from table_income where cardId =:cardId and cardDeleted = 0 order by id asc")
    fun readDataByCardId(cardId:Long):Flow<List<Income>>

    @Query("SELECT * FROM table_income where cardDeleted = 0 GROUP BY cardId ORDER BY _currentDate ASC")
    fun readUniqueCardId():Flow<List<Income>>*/


}

/*@Query("select * from table_income where dateLong=:dateLong order by id asc")
  fun readDataByDate(dateLong:String):LiveData<List<Income>>*/