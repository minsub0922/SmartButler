package com.kau.smartbutler.controller

import android.content.Context
import android.util.Log
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

class DeviceModeControllerAdpater (
        val mContext: Context,
        val modelList: ArrayList<Device>,
        var switchState:Boolean = false)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==-1) return EmptyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_empty, parent, false))
        return DeviceModeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_device_mode_list, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (position ==0) return -1
        return super.getItemViewType(position)
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        if (position==0) return

        val model = modelList.get(position)

        val holder = viewHolder as DeviceModeViewHolder

        holder.deviceName.text = model.name

        if (!switchState){// in home

            holder.leftButton.text = "그대로 유지"
            holder.rightButton.text = "꺼짐"

            if (!model.stateInHome){// default

                holder.rightButton.setTextColor(mContext.resources.getColor(R.color.AppThemeColor))
                holder.leftButton.setTextColor(mContext.resources.getColor(R.color.inActivatedColor))

            }else{

                holder.rightButton.setTextColor(mContext.resources.getColor(R.color.inActivatedColor))
                holder.leftButton.setTextColor(mContext.resources.getColor(R.color.AppThemeColor))

            }

        }else{

            holder.leftButton.text = "켜짐"
            holder.rightButton.text = "그대로 유지"

            if (!model.stateOutOfHome){// default

                holder.rightButton.setTextColor(mContext.resources.getColor(R.color.AppThemeColor))
                holder.leftButton.setTextColor(mContext.resources.getColor(R.color.inActivatedColor))

            }else{

                holder.rightButton.setTextColor(mContext.resources.getColor(R.color.inActivatedColor))
                holder.leftButton.setTextColor(mContext.resources.getColor(R.color.AppThemeColor))

            }


        }

        holder.leftButton.setOnClickListener {

            if (!switchState){// in home

                model.stateInHome = true

            }else{
                model.stateOutOfHome = true
            }
            holder.rightButton.setTextColor(mContext.resources.getColor(R.color.inActivatedColor))
            holder.leftButton.setTextColor(mContext.resources.getColor(R.color.AppThemeColor))

        }

        holder.rightButton.setOnClickListener {

            if (!switchState){// in home

                model.stateInHome = false

            }else{
                model.stateOutOfHome = false
            }
            holder.rightButton.setTextColor(mContext.resources.getColor(R.color.AppThemeColor))
            holder.leftButton.setTextColor(mContext.resources.getColor(R.color.inActivatedColor))

        }

    }

    class DeviceModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val deviceName: TextView by lazy { itemView.findViewById<TextView>(R.id.deviceNameTextView) }
        val leftButton: Button by lazy { itemView.findViewById<Button>(R.id.deviceModeLeftButton) }
        val rightButton: Button by lazy { itemView.findViewById<Button>(R.id.deviceModeRightButton) }

    }
}