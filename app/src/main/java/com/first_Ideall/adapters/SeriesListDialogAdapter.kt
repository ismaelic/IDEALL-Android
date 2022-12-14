package com.first_Ideall.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.first_Ideall.R
import com.first_Ideall.rooms.data.SeriesData

class SeriesListDialogAdapter :
    RecyclerView.Adapter<SeriesListDialogAdapter.SeriesDialogViewHolder>() {
    interface SeriesDialogItemClickListener {
        fun onClick(position: Int)
    }

    private lateinit var clickListener: SeriesDialogItemClickListener
    private var seriesList = listOf<SeriesData>()
    private var selectedIdx: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesDialogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.series_list_dialog_item_view, parent, false)

        return SeriesDialogViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesDialogViewHolder, position: Int) {
        if (position == 0) {
            holder.setNoSeriesText()
        } else {
            holder.setTextViewText(seriesList[position - 1].seriesTitle)
        }
        if (position == selectedIdx) {
            holder.setClickedView()
        } else {
            holder.resetView()
        }
        holder.itemView.setOnClickListener {
            clickListener.onClick(position)
        }
    }

    override fun getItemCount(): Int = seriesList.size + 1

    fun setSeriesList(list: List<SeriesData>) {
        seriesList = list
        notifyDataSetChanged()
    }

    fun setSelection(position: Int) {
        val prevIdx = selectedIdx
        selectedIdx = position
        notifyItemChanged(prevIdx)
        notifyItemChanged(selectedIdx)
    }

    fun setInitialSelect(id: Long?) {
        selectedIdx = when (id) {
            null -> 0
            else -> seriesList.indexOfFirst { data -> data.seriesId == id } + 1
        }
        notifyItemChanged(selectedIdx)
    }

    fun setClickListener(listener: SeriesDialogItemClickListener) {
        clickListener = listener
    }

    fun getSelectedSeriesId(): Long? {
        return if (selectedIdx == 0) {
            null
        } else {
            seriesList[selectedIdx - 1].seriesId
        }
    }

    class SeriesDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemTextView = itemView.findViewById<TextView>(R.id.seriesDialogItemTextView)
        private val unselectedBGColor = getColor(itemView.context, R.color.unselected_bg_color)
        private val unselectedTColor = getColor(itemView.context, R.color.unselected_txt_color)
        private val selectedTColor = getColor(itemView.context, R.color.selected_txt_color)
        private val selectedBGColor = getColor(itemView.context, R.color.selected_bg_color)

        fun setClickedView() {
            itemTextView.setBackgroundColor(selectedBGColor)
            itemTextView.setTextColor(selectedTColor)
        }

        fun resetView() {
            itemTextView.setBackgroundColor(unselectedBGColor)
            itemTextView.setTextColor(unselectedTColor)
        }

        fun setTextViewText(text: String) {
            itemTextView.text = text
        }

        fun setNoSeriesText() {
            itemTextView.text = itemView.context.getString(R.string.no_series)
        }
    }
}