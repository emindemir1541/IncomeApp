package com.emindev.expensetodolist.main.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.Month

@Dao
interface IncomeDao {

    @Upsert
    suspend fun upsert(income: Income)

    @Delete
    suspend fun delete(income: Income)

    @Query("Delete from table_income")
    suspend fun deleteAll()

    @Query("select * from table_income where deleted = 0 order by id asc")
    fun readAllData(): Flow<List<Income>>

    @Query("select * from table_income where _currentDate like '%' || :month || '/' || :year and deleted = 0 order by id asc")
    fun readSelectedData(month: String,year:String):Flow<List<Income>>

    @Query("select * from table_income where cardId =:cardId and deleted = 0 order by id asc")
    fun readDataByCardId(cardId:Long):Flow<List<Income>>

    @Query("SELECT * FROM table_income where deleted = 0 GROUP BY cardId ORDER BY _currentDate ASC")
    fun readUniqueCardId():Flow<List<Income>>


}

/*@Query("select * from table_income where dateLong=:dateLong order by id asc")
  fun readDataByDate(dateLong:String):LiveData<List<Income>>*/