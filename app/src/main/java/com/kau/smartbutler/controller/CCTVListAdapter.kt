package com.kau.smartbutler.controller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.CCTV
import com.kau.smartbutler.view.main.home.child.CCTVDetailActivity
import com.kau.smartbutler.view.main.home.child.DeviceModifiedActivity
import java.util.*

class CCTVListAdapter (
        val mContext: Context,
        val modelList: ArrayList<CCTV>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_device_list, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as DeviceListViewHolder

        holder.text.text = model.name

        holder.itemView.setOnClickListener{

            mContext.startActivity(Intent(mContext, CCTVDetailActivity::class.java).putExtra("cctv", model))

        }

    }


    class DeviceListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val text: TextView by lazy { itemView.findViewById<TextView>(R.id.deviceNameTextView) }

    }
}