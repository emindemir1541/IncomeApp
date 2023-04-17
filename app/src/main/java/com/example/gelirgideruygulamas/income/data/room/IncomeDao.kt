package com.example.gelirgideruygulamas.income.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gelirgideruygulamas.expense.data.room.Expense

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

   /* @Query("select * from table_expense where month =:month and year=:year order by id asc")
    fun readSelectedData(month: Int,year: Int):LiveData<List<Expense>>*/

    @Query("select * from table_income where cardId =:cardId order by id asc")
    fun readCardData(cardId:Long):LiveData<List<Income>>


}

/*@Query("select * from table_income where dateLong=:dateLong order by id asc")
  fun readDataByDate(dateLong:String):LiveData<List<Income>>*/