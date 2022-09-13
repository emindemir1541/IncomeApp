package com.example.gelirgideruygulamas.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.databinding.FragmentMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class FragmentMain(private val mContext: Context) : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myData = ArrayList<DataPoint>()
        for (i in 1..25) {
            myData.add(DataPoint(i.toDouble(), i.toDouble()))
        }

        updateProgressBar(60)

        binding.fragmentMainCardTotalIncome.setOnClickListener {
            setGraphDialog(myData.toTypedArray())
        }

    }


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


    /*   fun SetAlertDialogInvestiment() {
           val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
           alertDialogBuilder.setView(R.layout.layout_graph_investment)
           val alertDialog = alertDialogBuilder.create()
           alertDialog.show()
       }*/

    private fun updateProgressBar(progress: Int) {
        binding.fragmentMainProgressbar.progress = progress
        binding.fragmentMainProgressbarTextview.text = progress.toString()
    }
}