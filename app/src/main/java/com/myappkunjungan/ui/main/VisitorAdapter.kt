package com.myappkunjungan.ui.main

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.myappkunjungan.data.response.Visitor
import com.myappkunjungan.databinding.ItemRowVisitorBinding
import com.myappkunjungan.ui.addeditvisitor.AddEditVisitorActivity
import com.myappkunjungan.util.convertDate

class VisitorAdapter(private val visitorList: List<Visitor>): RecyclerView.Adapter<VisitorAdapter.ViewHolder>()  {

    class ViewHolder (val binding: ItemRowVisitorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRowVisitorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(visitorList[position]) {
                binding.apply {
                    textNik.text = nik
                    textName.text = name
                    textDate.text = convertDate(dateVisited)
                    textAddress.text = address

                    root.setOnClickListener {
                        val moveWithObjectIntent = Intent(itemView.context, AddEditVisitorActivity::class.java)
                        moveWithObjectIntent.putExtra("activity", "edit")
                        moveWithObjectIntent.putExtra("EXTRA_VISITOR", visitorList[position])
                        itemView.context.startActivity(moveWithObjectIntent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = visitorList.size
}