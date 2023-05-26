package com.emindev.expensetodolist.main.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Upsert
    suspend fun upsert(income: Income)

    @Query("Delete from table_income where id=:id ")
    suspend fun delete(id: Int)

    @Query("Delete from table_income")
    suspend fun deleteAll()

    @Query("select * from table_income order by id asc")
    fun readAllData(): Flow<List<Income>>

    @Query("select * from table_income where month =:month and year=:year order by id asc")
    fun readSelectedData(month: Int,year: Int):Flow<List<Income>>

    @Query("select * from table_income where cardId =:cardId order by id asc")
    fun readCardData(cardId:Long):Flow<List<Income>>


}

/*@Query("select * from table_income where dateLong=:dateLong order by id asc")
  fun readDataByDate(dateLong:String):LiveData<List<Income>>*/