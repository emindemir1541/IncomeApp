package com.example.gelirgideruygulamas.Fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gelirgideruygulamas.Adapter.RvExpenseAdapter
import com.example.gelirgideruygulamas.DataBases.MySharedPreferences
import com.example.gelirgideruygulamas.DataClass.Expense
import com.example.gelirgideruygulamas.DataClass.expenseType
import com.example.gelirgideruygulamas.MyHelper
import com.example.gelirgideruygulamas.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.android.synthetic.main.fragment_expense.*

class FragmentExpense(private val mContext: Context, private val mAppCompatActivity: AppCompatActivity, private val fab_add: FloatingActionButton, private val fab_editMode_back: FloatingActionButton, private val fab_editMode_delete: FloatingActionButton) :
    Fragment() {

    private lateinit var expenseUndoneList: ArrayList<Expense>
    private lateinit var expenseDoneList: ArrayList<Expense>
    private lateinit var expenseList: ArrayList<Expense>
    private lateinit var adapter: RvExpenseAdapter
    private var fullScreenDialog: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expense, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SetFABButton()
        SetRVExpense()
    }

    fun SetRVExpense() {
        rv_expense.setHasFixedSize(true)
        rv_expense.layoutManager = LinearLayoutManager(mContext)

        expenseUndoneList = ArrayList<Expense>()
        expenseUndoneList.add(Expense(1, "gider1", 55f, "12/10/2020", "25,9,2001", false, false, "jhdsbf", 5, expenseType.debt))

        expenseDoneList = ArrayList<Expense>()
        expenseDoneList.add(Expense(1, "gider2", 55f, "12/10/2020", "25,9,2001", true, false, "ejwdhn", 12, expenseType.need))

        expenseList = ArrayList<Expense>()
        expenseList.add(Expense(1, "gider3", 55f, "12/10/2020", "25,9,2001", true, true, "djshgb", 3, expenseType.debt))

        adapter = RvExpenseAdapter(mContext, mAppCompatActivity, expenseList, expenseUndoneList, expenseDoneList, fab_add, fab_editMode_back, fab_editMode_delete)
        rv_expense.adapter = adapter
    }

    fun SetFABButton() {
        // fab_add.animate(R.layout.layot_add_expense)
        fab_add.setOnClickListener {
            if (MySharedPreferences(mContext).getBottomNavigationLocation() == R.id.itemExpense)
                fab_add.hide()
            SetFullScreenDialogExpense()

        }
    }

    fun SetFullScreenDialogExpense() {

        if (fullScreenDialog == null) {


            fullScreenDialog = if (MyHelper.isDarkThemeOn(mContext)) {
                Dialog(mContext, android.R.style.Theme_Material_NoActionBar)
            }
            else {
                Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar)
            }


            fullScreenDialog?.setContentView(R.layout.layot_add_expense)
            fullScreenDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fullScreenDialog?.window?.setWindowAnimations(R.style.DialogAnimationScale)
            fullScreenDialog?.show()

            val layout_expense_add_expenseAdd = fullScreenDialog?.findViewById<TextView>(R.id.layout_expense_add_expenseAdd)
            val layout_expense_add_close = fullScreenDialog?.findViewById<FrameLayout>(R.id.layout_expense_add_close)
            val layout_expense_add_save = fullScreenDialog?.findViewById<MaterialButton>(R.id.layout_expense_add_save)
            val layout_expense_add_repetationType = fullScreenDialog?.findViewById<RadioGroup>(R.id.layout_expense_add_repetationType)
            val layout_expense_add_repetationType1 = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_repetationType1)
            val layout_expense_add_repetationType2 = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_repetationType2)
            val layout_expense_add_amount = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_amount)
            val layout_expense_add_date = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_date)
            val layout_expense_add_incomeName = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_incomeName)
            val layout_expense_add_lender = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_lender)
            val layout_expense_add_monthlyView = fullScreenDialog?.findViewById<LinearLayout>(R.id.layout_expense_add_monthlyView)
            val layout_expense_add_lenderView = fullScreenDialog?.findViewById<ConstraintLayout>(R.id.layout_expense_add_lenderView)
            val layout_expense_add_repetition = fullScreenDialog?.findViewById<EditText>(R.id.layout_expense_add_repetition)
            val layout_expense_add_delete = fullScreenDialog?.findViewById<Button>(R.id.layout_expense_add_delete)
            val layout_expense_add_done = fullScreenDialog?.findViewById<Button>(R.id.layout_expense_add_done)
            val layout_expense_add_everyMonty = fullScreenDialog?.findViewById<MaterialCheckBox>(R.id.layout_expense_add_everyMonty)
            val layout_expense_add_repetitionView = fullScreenDialog?.findViewById<ConstraintLayout>(R.id.layout_expense_add_repetitionView)
            val layout_expense_add_expenseType = fullScreenDialog?.findViewById<RadioGroup>(R.id.layout_expense_add_expenseType)
            val layout_expense_add_typeNeed = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_typeNeed)
            val layout_expense_add_typeWant = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_typeWant)
            val layout_expense_add_typeDebt = fullScreenDialog?.findViewById<MaterialRadioButton>(R.id.layout_expense_add_typeDebt)


            //başlangıçta yapılanlar
            layout_expense_add_repetationType2?.isChecked = true
            layout_expense_add_lenderView?.isVisible = false
            layout_expense_add_monthlyView?.isVisible = false
            layout_expense_add_delete?.isVisible = false
            layout_expense_add_done?.isVisible = false
            layout_expense_add_everyMonty!!.isChecked = true
            layout_expense_add_repetitionView?.isVisible = false
            layout_expense_add_typeWant?.isChecked = true
            layout_expense_add_lenderView?.isVisible = false



            layout_expense_add_everyMonty.setOnClickListener { layout_expense_add_repetitionView?.isVisible = !layout_expense_add_everyMonty.isChecked }

            layout_expense_add_expenseType!!.setOnCheckedChangeListener { _, _ ->
                if (layout_expense_add_typeNeed!!.isChecked) {
                    layout_expense_add_lenderView?.isVisible = false
                    // TODO: 17.04.2022 save sql
                }
                else if (layout_expense_add_typeWant!!.isChecked) {
                    layout_expense_add_lenderView?.isVisible = false
                    // TODO: 17.04.2022  sql save
                }
                else if (layout_expense_add_typeDebt!!.isChecked) {
                    layout_expense_add_lenderView?.isVisible = true
                    // TODO: 17.04.2022 save sql
                }
            }

            layout_expense_add_repetationType?.setOnCheckedChangeListener() { radioGroup, i ->
                if (layout_expense_add_repetationType1!!.isChecked) {
                    layout_expense_add_monthlyView!!.isVisible = true
                }
                if (layout_expense_add_repetationType2!!.isChecked) {
                    layout_expense_add_monthlyView!!.isVisible = false
                }
            }

            fullScreenDialog?.setOnCancelListener {
                fab_add.show()
                fullScreenDialog = null
            }

            layout_expense_add_close?.setOnClickListener {
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }

            layout_expense_add_save?.setOnClickListener {
                fullScreenDialog?.cancel()
                fullScreenDialog = null
            }

        }


    }

}