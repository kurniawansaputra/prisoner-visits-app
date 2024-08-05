package com.myappkunjungan.ui.report

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.myappkunjungan.R
import com.myappkunjungan.data.response.Visitor
import com.myappkunjungan.data.response.VisitorResponse
import com.myappkunjungan.data.retrofit.ApiConfig
import com.myappkunjungan.databinding.ActivityReportBinding
import com.myappkunjungan.databinding.LayoutDialogFilterByMonthBinding
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReportActivity : AppCompatActivity() {
    private val months = arrayOf(
        "Januari" to "1",
        "Februari" to "2",
        "Maret" to "3",
        "April" to "4",
        "Mei" to "5",
        "Juni" to "6",
        "Juli" to "7",
        "Agustus" to "8",
        "September" to "9",
        "Oktober" to "10",
        "November" to "11",
        "Desember" to "12"
    )
    private var selectedMonth: String? = getDefaultMonthValue()
    private var selectedYear: String? = getDefaultYearValue()
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        optionMenu()
        setToolbar()
//        getCount()
        getReport()
    }

    private fun setToolbar() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun optionMenu() {
        binding.apply {
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuFilter -> {
                        filterByMonth()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun getDefaultMonthValue(): String {
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        return months[currentMonth].second
    }

    private fun getDefaultYearValue(): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
    }

    private fun filterByMonth() {
        val binding: LayoutDialogFilterByMonthBinding = LayoutDialogFilterByMonthBinding.inflate(layoutInflater)
        val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        binding.apply {
            val monthAdapter = ArrayAdapter(this@ReportActivity, R.layout.list_item, months.map { it.first })
            binding.autoMonth.setAdapter(monthAdapter)

            val defaultMonthPosition = months.indexOfFirst { it.second == selectedMonth }.let { if (it == -1) 0 else it }
            binding.autoMonth.setText(monthAdapter.getItem(defaultMonthPosition).toString(), false)

            autoMonth.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedMonth = months[position].second
            }

            val years = ArrayList<String>()
            val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
            for (i in 2023..thisYear) {
                years.add(i.toString())
            }

            val yearAdapter = ArrayAdapter(this@ReportActivity, R.layout.list_item, years)
            binding.autoYear.setAdapter(yearAdapter)

            val defaultYearPosition = years.indexOf(selectedYear).let { if (it == -1) 0 else it }
            binding.autoYear.setText(yearAdapter.getItem(defaultYearPosition).toString(), false)

            autoYear.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedYear = years[position]
            }

            buttonBack.text = "Kembali"
            buttonBack.setOnClickListener {
                dialog.dismiss()
            }
            buttonFilter.setOnClickListener {
                getReport()
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun getReport() {
        setProgressBar(true)
        val client = ApiConfig.getApiService().getVisitorsByMonth(selectedMonth, selectedYear)
        client.enqueue(object : retrofit2.Callback<VisitorResponse> {
            override fun onResponse(call: Call<VisitorResponse>, response: Response<VisitorResponse>) {
                setProgressBar(false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val data = response.body()?.data

                    if (status == true) {
                        val reportAdapter = ReportAdapter(data as ArrayList<Visitor>)
                        binding.rvVisitor.visibility = View.VISIBLE
                        binding.rvVisitor.adapter = reportAdapter
                        binding.rvVisitor.setHasFixedSize(true)
                    }
                }
            }

            override fun onFailure(call: Call<VisitorResponse>, t: Throwable) {
                setProgressBar(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

//    private fun getCount() {
//        setProgressBar(true)
//        val client = ApiConfig.getApiService().getCountVisitors()
//        client.enqueue(object : retrofit2.Callback<CountVisitorResponse> {
//            override fun onResponse(call: Call<CountVisitorResponse>, response: Response<CountVisitorResponse>) {
//                setProgressBar(false)
//                if (response.isSuccessful) {
//                    val status = response.body()?.status
//                    val data = response.body()?.data
//
//                    if (status == true && data != null) {
//                        setBarChart(data)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<CountVisitorResponse>, t: Throwable) {
//                setProgressBar(false)
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

//    private fun setBarChart(data: List<CountVisitor>) {
//        val dataByMonth: MutableList<BarEntry> = ArrayList()
//        val xAxisLabels: MutableList<String> = ArrayList()
//
//        data.forEachIndexed { index, countVisitor ->
//            dataByMonth.add(BarEntry(index.toFloat(), countVisitor.total.toFloat()))
//            xAxisLabels.add("${countVisitor.month} ${countVisitor.year}")
//        }
//
//        val barDataSet = BarDataSet(dataByMonth, "Pengunjung")
//        barDataSet.valueFormatter = DefaultValueFormatter(0)
//        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.asList()
//        barDataSet.valueTextSize = 14f
//
//        val barData = BarData(barDataSet)
//
//        binding.apply {
//            chartViewEdu.data = barData
//            chartViewEdu.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//            chartViewEdu.setFitBars(true)
//            chartViewEdu.description.text = ""
//            chartViewEdu.animateY(2000)
//        }
//    }

    private fun setProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    companion object {
        private val TAG = ReportActivity::class.java.simpleName
    }
}