package com.example.gelirgideruygulamas.data.income

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(income: Income)

    @Update
    suspend fun update(income: Income)

    @Query("Delete from table_income where id=:id ")
    suspend fun delete(id: Int)

    @Query("Delete from table_income")
    suspend fun deleteAll()

    @Query("select * from table_income order by id asc")
    fun readAllData(): LiveData<List<Income>>

    @Query("select * from table_income where cardId =:cardId order by id asc")
    fun readCardData(cardId:Long):LiveData<List<Income>>

    @Query("Update table_income set dataChanged =:dataChanged")
    fun refreshData(dataChanged: Long)
}

/*@Query("select * from table_income where dateLong=:dateLong order by id asc")
  fun readDataByDate(dateLong:String):LiveData<List<Income>>*/