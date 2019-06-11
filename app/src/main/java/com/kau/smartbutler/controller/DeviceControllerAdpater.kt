package com.kau.smartbutler.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.Device
import java.util.*

class DeviceControllerAdpater (
        val mContext: Context,
        val modelList: ArrayList<Device>,
        val toggleSwitch: ObservableField<Boolean>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

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

        setButtonBackground(model.type, model.state, holder)

        holder.deviceSwitch.isChecked = model.state

        holder.deviceSwitch.setOnClickListener { v -> toggleButtonListener(holder, model) }

        holder.name.text = model.name

    }

    private fun setButtonBackground(type: Int, switch: Boolean, holder:DeviceViewHolder){

        when(type){
            1 -> {
                if (switch) holder.button.background = mContext.getDrawable(R.drawable.btn_bedroom_on)
                else holder.button.background = mContext.getDrawable(R.drawable.btn_bedroom_off)
            }
            2 -> {
                if (switch) holder.button.background = mContext.getDrawable(R.drawable.btn_tv_on)
                else holder.button.background = mContext.getDrawable(R.drawable.btn_tv_off)
            }
            3 -> {
                if (switch) holder.button.background = mContext.getDrawable(R.drawable.btn_aircon_on)
                else holder.button.background = mContext.getDrawable(R.drawable.btn_aircon_off)
            }
            4 -> {
                if (switch) holder.button.background = mContext.getDrawable(R.drawable.btn_library_on)
                else holder.button.background = mContext.getDrawable(R.drawable.btn_library_off)
            }
        }

        if (switch) {
            holder.name.alpha = 1f
        }
        else {

            holder.name.alpha = 0.5f
        }

    }

    private fun toggleButtonListener(holder: DeviceViewHolder, model: Device){

        model.state = !(model.state)

        setButtonBackground(model.type, model.state, holder)

    }

    class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var button: ImageView
        var name : TextView
        var deviceSwitch: Switch

        init {
            button = itemView.findViewById<ImageView>(R.id.btn_device_control)
            name = itemView.findViewById<TextView>(R.id.txt_device_name)
            deviceSwitch = itemView.findViewById<Switch>(R.id.switch_device)
        }

    }

    class AddDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val button: Button by lazy { itemView.findViewById<Button>(R.id.btn_device_control) }

    }
}