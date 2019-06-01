package com.kau.smartbutler.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.Device
import kotlinx.android.synthetic.main.model_device_control.view.*
import java.util.*

class DeviceControllerAdpater (val mContext: Context, val modelList: ArrayList<Device>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_device_control, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val holder = viewHolder as DeviceViewHolder
        val model = modelList.get(position)

        holder.button.background = mContext.getDrawable(R.drawable.btn_n)

     }

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val button: Button by lazy { itemView.findViewById<Button>(R.id.btn_device_control) }

    }
}