package com.emindev.expensetodolist.main.data.room.income

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Upsert
    suspend fun upsert(incomeModel: IncomeModel):Long

    @Upsert
    suspend fun upsert(incomeCardModel: IncomeCardModel):Long
/*
    @Delete
    suspend fun delete(income: Income)
*/

    @Query("Delete from table_income")
    suspend fun deleteAll()

   @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.amount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income.isRepeatable " +
           " from table_income left join table_income_card on table_income.id = table_income_card.id " +
           "where deleted = 0 order by cardId")
    fun readAllData(): Flow<List<Income>>

    @Query("select table_income.id,table_income_card.cardId,table_income.name,table_income_card.amount,table_income.initialDate,table_income_card.currentDate,table_income.deleted,table_income.isRepeatable " +
            " from table_income left join table_income_card on table_income.id = table_income_card.id " +
            "where deleted = 0 and currentDate like :year ||  '/' || :month ||'%' order by cardId")
    fun getIncomesBySelectedDate(month: String,year: String): Flow<List<Income>>
    @Query("select * from table_income_card where id =:incomeId order by currentDate")
    fun getCardsByIncome(incomeId:Long):Flow<List<IncomeCardModel>>




/*
    @Query("select * from table_income where cardId =:cardId and deleted = 0 order by id asc")
    fun readDataByCardId(cardId:Long):Flow<List<Income>>

    @Query("SELECT * FROM table_income where deleted = 0 GROUP BY cardId ORDER BY _currentDate ASC")
    fun readUniqueCardId():Flow<List<Income>>*/


}

/*@Query("select * from table_income where dateLong=:dateLong order by id asc")
  fun readDataByDate(dateLong:String):LiveData<List<Income>>*/