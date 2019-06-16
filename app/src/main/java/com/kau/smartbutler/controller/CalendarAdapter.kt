package com.kau.smartbutler.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.CalendarItem
import kotlin.collections.ArrayList

class CalendarAdapter (
        val mContext: Context,
        val modelList: ArrayList<CalendarItem>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.model_calendar_date, parent, false)
        val height = parent.measuredHeight / 6
        itemView.minimumHeight = height
        return CalendarItemViewHolder(itemView)
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as CalendarItemViewHolder

        holder.dateTextView.text = model.date
        holder.dietAddedButton.visibility = if (model.registered) View.VISIBLE else View.INVISIBLE

    }

    class CalendarItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val dateTextView : TextView by lazy { itemView.findViewById<TextView>(R.id.dateTextView) }
        val dietAddedButton : Button by lazy { itemView.findViewById<Button>(R.id.dietAddedButton) }
    }
}