package com.kau.smartbutler.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R

class SpinnerItemAdapter (
        val mContext: Context,
        val modelList: Array<String>,
        var selectedPosition: Int,
        val itemClickListener: ItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SpinnerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_spinner_item, parent, false))
    }

    public interface ItemClickListener {
        fun onClick(pos: Int)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val v = holder as SpinnerViewHolder
        val model = modelList[position]

        v.text.text = model

        v.text.setTextColor(
                if (position == selectedPosition) mContext.resources.getColor(R.color.white)
                else mContext.resources.getColor(R.color.spinnerTextColor)
        )

        v.background.setBackgroundColor(
                if (position == selectedPosition) mContext.resources.getColor(R.color.AppThemeColor)
                else mContext.resources.getColor(R.color.transparent)
        )

        v.itemView.setOnClickListener { itemClickListener.onClick(position) }
    }



    class SpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text : TextView by lazy { itemView.findViewById<TextView>(R.id.text) }
        val background: ConstraintLayout by lazy { itemView.findViewById<ConstraintLayout>(R.id.background) }
    }
}