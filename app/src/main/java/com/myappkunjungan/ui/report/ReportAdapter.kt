package com.myappkunjungan.ui.report

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.myappkunjungan.data.response.Visitor
import com.myappkunjungan.databinding.ItemRowReportBinding
import com.myappkunjungan.util.convertDate

class ReportAdapter(private val visitorList: List<Visitor>) : RecyclerView.Adapter<ReportAdapter.ViewHolder>()  {
    class ViewHolder (val binding: ItemRowReportBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = visitorList.size

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(visitorList[position]) {
                binding.apply {
                    textNo.text = (position + 1).toString() + "."
                    textDate.text = convertDate(dateVisited)
                    textName.text = "$name ($nik)"
                    textPrisonerNo.text = prisonerNumber
                    textItems.text = luggage
                }
            }
        }
    }
}