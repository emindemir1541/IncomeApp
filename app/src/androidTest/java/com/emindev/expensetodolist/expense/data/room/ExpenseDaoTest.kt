package com.emindev.expensetodolist.expense.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.emindev.expensetodolist.expense.common.constant.ExpenseType
import com.emindev.expensetodolist.main.common.constant.RepeatType
import com.emindev.expensetodolist.main.common.helper.DateUtil
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.isMonthAndYearEqualTo
import com.emindev.expensetodolist.main.common.helper.DateUtil.Companion.dayOrMonthToValidString
import com.emindev.expensetodolist.main.common.util.SqlDateUtil
import com.emindev.expensetodolist.main.data.room.FinanceDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class ExpenseDaoTest {

    private lateinit var database: FinanceDatabase
    private lateinit var dao: ExpenseDao

    private val date = DateUtil.localDateNow


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FinanceDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.expenseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun upsertExpenseModel() = runTest {
        val expenseModelList = listOf(
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 1),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.LIMITED, 5, ExpenseType.UNNECESSARY, "", id = 2),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.NEED, "", id = 3),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.DEBT, "lender", id = 4),
        )

        expenseModelList.forEach {
            dao.upsert(it)
        }

        val expenseModelAll = dao.getExpenseModels().first()
        assert(expenseModelAll.containsAll(expenseModelList)) { expenseModelAll }

    }

    @Test
    fun upsertExpenseCardModel() = runTest {

        val expenseCardModelList = listOf(
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 1),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 2),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 3),
        )
        expenseCardModelList.forEach {
            dao.upsert(it)
        }

        val expenseModelAll = dao.getExpenseCardModels().first()

        assert(expenseModelAll.containsAll(expenseCardModelList)) { expenseModelAll }
    }

    @Test
    fun updateAmountOfCardsAfterSpecificDate() = runTest {

        val expenseCardModelList = listOf(
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 1),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 2),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 3),
        )
        expenseCardModelList.forEach {
            dao.upsert(it)
        }

        dao.updateAmountOfCardsAfterSpecificDate(1, SqlDateUtil.convertDate(date), 300f)

        val expenseCardModelAll = dao.getExpenseCardModels().first()

        expenseCardModelAll.filter { expenseCardModel: ExpenseCardModel -> expenseCardModel.id == 1L }.forEach {
            assert(it.currentAmount == 300f)
        }
    }

    @Test
    fun getInfinityExpenseModelsNotDeleted() = runTest {
        val expenseModelList = listOf(
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 1),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.LIMITED, 5, ExpenseType.UNNECESSARY, "", id = 2),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), true, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 3),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.DEBT, "lender", id = 4),
        )

        expenseModelList.forEach {
            dao.upsert(it)
        }
        val models = dao.getInfinityExpenseModelsNotDeleted().first()



        assert(models.find { expenseModel: ExpenseModel -> expenseModel.repeatType != RepeatType.INFINITY } == null)
        assert(models.find { expenseModel: ExpenseModel -> expenseModel.deleted } == null)
        assert(models.size == 1)
    }

    @Test
    fun getExpenseWitOneCardBySelectedDate() = runTest {
        val expenseModelList = listOf(
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.NEED, "", id = 1),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.LIMITED, 5, ExpenseType.UNNECESSARY, "", id = 2),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), true, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 3),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.DEBT, "lender", id = 4),
        )

        val expenseCardModelList = listOf(
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 1),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 2, 2),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 4, 3),
        )
        expenseModelList.forEach {
            dao.upsert(it)
        }
        expenseCardModelList.forEach {
            dao.upsert(it)
        }


        val expenseList = dao.getExpenseWithOneCardBySelectedDateNotDeleted(date.monthValue.dayOrMonthToValidString(), date.year.toString(), SqlDateUtil.dateDelimiter).first()

        assert(expenseList.isNotEmpty()) { expenseList }

        expenseList.forEach { expense: Expense ->
            assert(expense.repeatType == RepeatType.ONCE)
            assert(!expense.deleted)
            assert(expense.currentLocalDate.isMonthAndYearEqualTo(date))
        }
    }

    @Test
    fun getExpenseWithMultipleCardBySelectedDate() = runTest {
        val expenseModelList = listOf(
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.NEED, "", id = 1),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 2),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), true, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 3),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.DEBT, "lender", id = 4),
        )

        val expenseCardModelList = listOf(
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 1),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 2, 2),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 3, 3),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 4, 3),
        )
        expenseModelList.forEach {
            dao.upsert(it)
        }
        expenseCardModelList.forEach {
            dao.upsert(it)
        }


        val expenseList = dao.getExpenseWithMultipleCardBySelectedDateNotDeleted(date.monthValue.dayOrMonthToValidString(), date.year.toString(), SqlDateUtil.dateDelimiter).first()

        assert(expenseList.isNotEmpty()) { expenseList }

        expenseList.forEach { expense: Expense ->
            assert(expense.repeatType != RepeatType.ONCE)
            assert(expense.currentLocalDate.isMonthAndYearEqualTo(date))
        }
    }  @Test
    fun getAllExpensesNotDeletedBySelectedDate() = runTest {
        val expenseModelList = listOf(
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.NEED, "", id = 1),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 2),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), true, RepeatType.INFINITY, null, ExpenseType.NEED, "", id = 3),
            ExpenseModel("name", 100f, SqlDateUtil.convertDate(date), false, RepeatType.ONCE, null, ExpenseType.DEBT, "lender", id = 4),
        )

        val expenseCardModelList = listOf(
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 1, 1),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 2, 2),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 3, 3),
            ExpenseCardModel(200f, SqlDateUtil.convertDate(date), false, false, 4, 3),
        )
        expenseModelList.forEach {
            dao.upsert(it)
        }
        expenseCardModelList.forEach {
            dao.upsert(it)
        }


        val expenseList = dao.getAllExpensesNotDeletedBySelectedDate(date.monthValue.dayOrMonthToValidString(), date.year.toString(), SqlDateUtil.dateDelimiter).first()

        assert(expenseList.size==3) { expenseList }

        expenseList.forEach { expense: Expense ->
            assert(expense.currentLocalDate.isMonthAndYearEqualTo(date))
        }
    }


}