package com.kau.smartbutler.controller

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kau.smartbutler.R
import com.kau.smartbutler.model.Conversation
import com.kau.smartbutler.model.Hospital
import com.kau.smartbutler.view.main.life.child.ReservationHospitalActivity
import java.util.*

class ConversationAdapter (
        val mContext: Context,
        val modelList: ArrayList<Conversation>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun getItemViewType(position: Int): Int {
        return modelList.get(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) return ConversationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_butler, parent, false))
        else return ConversationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_user, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as ConversationViewHolder

        holder.content.text = model.content

    }

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var content: TextView
        init {
            content = itemView.findViewById<TextView>(R.id.tv_conversation)
        }

    }

}