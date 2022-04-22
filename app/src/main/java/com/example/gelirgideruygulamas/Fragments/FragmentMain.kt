package com.example.gelirgideruygulamas.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gelirgideruygulamas.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentMain(private val mContext: Context) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*  val myDatas = ArrayList<DataPoint>()
          for (i in 1..25) {
              myDatas.add(DataPoint(i.toDouble(), i.toDouble()))
          }

          SetGraphDialog(myDatas.toTypedArray())
          UpdateProgressBar(60)*/

    }


    fun SetGraphDialog(graphDatas: Array<DataPoint>) {
        val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
        alertDialogBuilder.setView(R.layout.layout_graph_income)
        val graphDialog = alertDialogBuilder.create()
        graphDialog.show()

        val layout_graph_graphView = graphDialog.findViewById<GraphView>(R.id.layout_graph_income_graphView)
        val series = LineGraphSeries(graphDatas)
        //arrayOf(DataPoint(0.0, 1.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0))
        layout_graph_graphView?.addSeries(series)


    }


 /*   fun SetAlertDialogInvestiment() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(mContext)
        alertDialogBuilder.setView(R.layout.layout_graph_investment)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }*/

    fun UpdateProgressBar(progress: Int) {
        fragment_main_progressbar.progress = progress
        fragment_main_progressbar_textview.text = progress.toString()
    }
}