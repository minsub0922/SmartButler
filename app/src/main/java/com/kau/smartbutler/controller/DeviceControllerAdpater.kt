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

    override fun getItemViewType(position: Int): Int {
        return modelList.get(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) return AddDeviceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_add_device, parent, false))
        else return DeviceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_device_control, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        if (model.type==0) return

        val holder = viewHolder as DeviceViewHolder

        when(model.type){
            1 -> holder.button.background = mContext.getDrawable(R.drawable.btn_bedroom_on)
            2 -> holder.button.background = mContext.getDrawable(R.drawable.btn_tv_on)
            3 -> holder.button.background = mContext.getDrawable(R.drawable.btn_aircon_on)
            4 -> holder.button.background = mContext.getDrawable(R.drawable.btn_library_on)
        }

     }

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val button: Button by lazy { itemView.findViewById<Button>(R.id.btn_device_control) }

    }

    class AddDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val button: Button by lazy { itemView.findViewById<Button>(R.id.btn_device_control) }

    }
}