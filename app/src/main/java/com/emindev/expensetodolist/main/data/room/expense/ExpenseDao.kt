package com.emindev.expensetodolist.main.data.room.expense

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("select * from table_expense order by id asc")
     fun readData(): Flow<List<Expense>>

    @Query("select * from table_expense where cardId =:cardId order by id asc")
     fun readDataByCardId(cardId:Long):Flow<List<Expense>>

    @Query("select * from table_expense where month =:month and year=:year order by id asc")
    fun readSelectedData(month: Int,year: Int):Flow<List<Expense>>





}

/*    @Query("select DISTINCT startedDateLong from table_expense ORDER by startedDateLong ASC") // id olarak kullanılıyor
    fun readSavedData(): LiveData<List<SavedDate>>*/

/*@Query("Delete from table_expense")
suspend fun deleteAll()*/

/*@Query("Delete from table_expense where id=:id ")
suspend fun delete(id: Int)*/

/*    @Query("SELECT * FROM table_expense where month =:month AND year=:year")
    fun readDataByDate(month:Int,year:Int):LiveData<List<Expense>>*/