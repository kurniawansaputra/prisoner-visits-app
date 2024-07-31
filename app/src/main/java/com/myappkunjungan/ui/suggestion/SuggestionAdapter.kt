package com.myappkunjungan.ui.suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myappkunjungan.data.response.Suggestion
import com.myappkunjungan.databinding.ItemRowSuggestionBinding

class SuggestionAdapter(private val suggestionList: List<Suggestion>): RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {

    class ViewHolder (val binding: ItemRowSuggestionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemRowSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(suggestionList[position]) {
                binding.apply {
                    textName.text = name
                }
            }
        }
    }

    override fun getItemCount(): Int = suggestionList.size
}