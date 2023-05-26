package com.emindev.expensetodolist.main.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emindev.expensetodolist.R
import com.emindev.expensetodolist.main.common.calculation.Calculator
import com.emindev.expensetodolist.main.common.constant.Currency
import com.emindev.expensetodolist.main.data.room.Expense
import com.emindev.expensetodolist.main.data.room.Income
import com.emindev.expensetodolist.main.data.room.IncomeViewModel
import com.emindev.expensetodolist.databinding.FragmentMainBinding
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil
import com.emindev.expensetodolist.helperlibrary.common.helper.DateUtil.checkMonthAndYear
import com.emindev.expensetodolist.helperlibrary.common.helper.Helper.clearZero
import com.emindev.expensetodolist.main.ui.page.MainPage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class FragmentMain(private val mContext: Context) : Fragment() {

    private lateinit var composeView:ComposeView

    private lateinit var binding: FragmentMainBinding
    private var incomeList: List<Income> = emptyList()
    private var expenseList: List<Expense> = emptyList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false))
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        composeView.setContent {

        }

        //incomeGraph()
        calculator()


    }


    @SuppressLint("SetTextI18n")
    private fun calculator() {
      /*  incomeViewModel.readAllData.observe(viewLifecycleOwner) { incomeList ->
            this.incomeList = incomeList.filter { expense-> expense.date.checkMonthAndYear(DateUtil.currentDateTime) }
            val monthlyCalculator = Calculator(incomeList, expenseList, mContext)
            binding.fragmentMainTotalIncome.text = monthlyCalculator.totalIncome.clearZero() + Currency.TL
            binding.fragmentMainRemainingMoney.text = monthlyCalculator.remainedMoney.clearZero() + Currency.TL
            updateProgressBar(monthlyCalculator.totalIncome, monthlyCalculator.remainedMoney)
        }

        expenseViewModel.readAllData.observe(viewLifecycleOwner) { expenseList ->
            this.expenseList = expenseList.filter { expense-> expense.date.checkMonthAndYear(DateUtil.currentDateTime) }
            val monthlyCalculator = Calculator(incomeList, expenseList, mContext)
            binding.fragmentMainPaidExpense.text = monthlyCalculator.paidExpense.clearZero() + Currency.TL
            binding.fragmentMainPotentialExpense.text = monthlyCalculator.potentialExpense.clearZero() + Currency.TL
            binding.fragmentMainRemainingMoney.text = monthlyCalculator.remainedMoney.clearZero() + Currency.TL
            updateProgressBar(monthlyCalculator.totalIncome, monthlyCalculator.remainedMoney)


        }*/
    }

    /*private fun incomeGraph() {
        calculator()
        val incomeDataList = ArrayList<DataPoint>()
        for (i in 1..25) {
            incomeDataList.upsert(DataPoint(i.toDouble(), i.toDouble()))
        }

        // TODO: eski tarihe para ekleyince göstermiyor 
        // TODO: grafikler düzgün çalışmıyor

        incomeList.forEach { income ->
            incomeDataList.upsert(DataPoint(income.month.toDouble(), income.amount.toDouble()))
        }



        binding.fragmentMainCardTotalIncome.setOnClickListener {
            setGraphDialog(incomeDataList.toTypedArray())
        }
    }*/

    private fun setGraphDialog(graphData: Array<DataPoint>) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
        alertDialogBuilder.setView(R.layout.layout_graph_income)
        val graphDialog = alertDialogBuilder.create()
        graphDialog.show()

        val layout_graph_graphView = graphDialog.findViewById<GraphView>(R.id.layout_graph_income_graphView)
        val series = LineGraphSeries(graphData)
        //arrayOf(DataPoint(0.0, 1.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0))
        layout_graph_graphView?.addSeries(series)

        // TODO: 7.08.2022 set graphic
    }


    /* fun SetAlertDialogInvestiment() {
         val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
         alertDialogBuilder.setView(R.layout.layout_graph_investment)
         val alertDialog = alertDialogBuilder.create()
         alertDialog.show()
     }*/

    @SuppressLint("SetTextI18n")
    private fun updateProgressBar(income: Float, remainedMoney: Float) {
        binding.fragmentMainProgressbar.progress = ((remainedMoney * 100) / income).toInt()
        binding.fragmentMainProgressbarTextview.text = remainedMoney.clearZero() + Currency.TL
    }
}