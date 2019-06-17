package com.kau.smartbutler.controller

import android.content.Context
import android.content.Intent
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
import com.kau.smartbutler.model.PillCaution
import com.kau.smartbutler.view.main.home.child.DeviceModifiedActivity
import java.util.*

class PillCautionListAdapter (
        val mContext: Context,
        val modelList: ArrayList<PillCaution>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PillCautionListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_pill_caution, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as PillCautionListViewHolder

        holder.text.text = (model.component + "과 충돌 성분이 있습니다.")


    }


    class PillCautionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val text: TextView by lazy { itemView.findViewById<TextView>(R.id.deviceNameTextView) }

    }
}