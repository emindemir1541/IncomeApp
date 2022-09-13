package com.example.gelirgideruygulamas.data.expense

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    fun delete(expense: Expense)

    @Query("select * from table_expense order by id asc") // TODO: 22.07.2022 sql kodunu ayarla
    fun readData(): LiveData<List<Expense>>

    @Query("Update table_expense set dataChanged =:dataChanged")
    fun refreshData(dataChanged: Long)


}

/*    @Query("select DISTINCT startedDateLong from table_expense ORDER by startedDateLong ASC") // id olarak kullanılıyor
    fun readSavedData(): LiveData<List<SavedDate>>*/

/*@Query("Delete from table_expense")
suspend fun deleteAll()*/

/*@Query("Delete from table_expense where id=:id ")
suspend fun delete(id: Int)*/

/*    @Query("SELECT * FROM table_expense where month =:month AND year=:year")
    fun readDataByDate(month:Int,year:Int):LiveData<List<Expense>>*/