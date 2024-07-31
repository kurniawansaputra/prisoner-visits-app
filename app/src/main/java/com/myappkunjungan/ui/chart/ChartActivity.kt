package com.myappkunjungan.ui.chart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.myappkunjungan.R
import com.myappkunjungan.data.response.CountVisitor
import com.myappkunjungan.data.response.CountVisitorResponse
import com.myappkunjungan.data.retrofit.ApiConfig
import com.myappkunjungan.databinding.ActivityChartBinding
import retrofit2.Call
import retrofit2.Response

class ChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setToolbar()
        getCount()
    }

    private fun setToolbar() {
        binding.apply {
            topAppBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun getCount() {
        setProgressBar(true)
        val client = ApiConfig.getApiService().getCountVisitors()
        client.enqueue(object : retrofit2.Callback<CountVisitorResponse> {
            override fun onResponse(call: Call<CountVisitorResponse>, response: Response<CountVisitorResponse>) {
                setProgressBar(false)
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val data = response.body()?.data

                    if (status == true && data != null) {
                        setBarChart(data)
                    }
                }
            }

            override fun onFailure(call: Call<CountVisitorResponse>, t: Throwable) {
                setProgressBar(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setBarChart(data: List<CountVisitor>) {
        val dataByMonth: MutableList<BarEntry> = ArrayList()
        val xAxisLabels: MutableList<String> = ArrayList()

        data.forEachIndexed { index, countVisitor ->
            dataByMonth.add(BarEntry(index.toFloat(), countVisitor.total.toFloat()))
            xAxisLabels.add("${countVisitor.month} ${countVisitor.year}")
        }

        val barDataSet = BarDataSet(dataByMonth, "Pengunjung")
        barDataSet.valueFormatter = DefaultValueFormatter(0)
        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.asList()
        barDataSet.valueTextSize = 14f

        val barData = BarData(barDataSet)

        binding.apply {
            chartViewEdu.data = barData
            chartViewEdu.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            chartViewEdu.setFitBars(true)
            chartViewEdu.description.text = ""
            chartViewEdu.animateY(2000)
        }
    }

    private fun setProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    companion object {
        private val TAG = ChartActivity::class.java.simpleName
    }
}