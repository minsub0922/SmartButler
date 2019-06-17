package com.kau.smartbutler.controller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.Hospital
import com.kau.smartbutler.view.main.life.child.ReservationHospitalActivity
import java.util.*

class ReservationSearchControllerAdapter (
        val mContext: Context,
        val modelList: ArrayList<Hospital>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun getItemViewType(position: Int): Int {
        return modelList.get(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HospitalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_hostipal_search, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as HospitalViewHolder

        if (model.type == 0) {
            holder.itemView.setOnClickListener{
                mContext.startActivity(Intent(mContext, ReservationHospitalActivity::class.java))
            }
        }
        holder.name.text = model.name
        holder.address.text = model.address
        holder.hospitalAdd.setOnClickListener {

        }

    }

    class HospitalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var name : TextView
        var address : TextView
        var hospitalAdd : ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.tv_hospital_name)
            address = itemView.findViewById<TextView>(R.id.tv_hospital_address)
            hospitalAdd = itemView.findViewById<ImageView>(R.id.iv_add_n)
        }

    }

}