package com.example.gelirgideruygulamas.data.expense

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("Delete from table_expense where cardId =:cardId")
    suspend fun deleteByCardId(cardId: Long)

    @Query("select * from table_expense order by id asc") // TODO: 22.07.2022 sql kodunu ayarla
     fun readData(): LiveData<List<Expense>>

    @Query("select * from table_expense where cardId =:cardId order by id asc")
     fun readDataByCardId(cardId:Long):LiveData<List<Expense>>

    @Query("Update table_expense set dataChanged =:dataChanged")
    suspend fun refreshData(dataChanged: Long)




}

/*    @Query("select DISTINCT startedDateLong from table_expense ORDER by startedDateLong ASC") // id olarak kullanılıyor
    fun readSavedData(): LiveData<List<SavedDate>>*/

/*@Query("Delete from table_expense")
suspend fun deleteAll()*/

/*@Query("Delete from table_expense where id=:id ")
suspend fun delete(id: Int)*/

/*    @Query("SELECT * FROM table_expense where month =:month AND year=:year")
    fun readDataByDate(month:Int,year:Int):LiveData<List<Expense>>*/