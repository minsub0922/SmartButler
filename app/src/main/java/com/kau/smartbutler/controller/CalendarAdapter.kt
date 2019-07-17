package com.kau.smartbutler.controller

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.CalendarItem
import com.kau.smartbutler.view.main.life.child.DietManagementActivity
import com.kau.smartbutler.view.main.life.child.DietOrderActivity
import kotlinx.android.synthetic.main.activity_health_care.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter (
        val mContext: Context,
        val modelList: ArrayList<CalendarItem>,
        val type: String)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun getItemViewType(position: Int): Int {
        return modelList.get(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val height = parent.measuredHeight / 6
        if (viewType == 1) {
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.model_calendar_date, parent, false)
            itemView.minimumHeight = height
            return CalendarItemViewHolder(itemView)
        }
        else {
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.model_calendar_before, parent, false)
            itemView.minimumHeight = height
            return CalendarItemViewHolder(itemView)
        }
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as CalendarItemViewHolder

        holder.dateTextView.text = model.date
        holder.dietAddedButton.visibility = if (model.registered) View.VISIBLE else View.INVISIBLE
        holder.itemView.setOnClickListener {
            if(type == "manage") {
                val currentTime = System.currentTimeMillis()
                val currentDate = Date(currentTime)
                val cal = GregorianCalendar()
                cal.time = currentDate
                val i = Intent(mContext, DietManagementActivity::class.java)
                i.putExtra("year", cal.get(Calendar.YEAR))
                i.putExtra("month", cal.get(Calendar.MONTH))
                i.putExtra("date", cal.get(Calendar.DAY_OF_MONTH))
                i.putExtra("date", currentTime)
                mContext.startActivity(i)
            }
            else
                mContext.startActivity(Intent(mContext, DietOrderActivity::class.java))
        }

    }

    class CalendarItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dateTextView : TextView by lazy { itemView.findViewById<TextView>(R.id.dateTextView) }
        val dietAddedButton : Button by lazy { itemView.findViewById<Button>(R.id.dietAddedButton) }
    }
}