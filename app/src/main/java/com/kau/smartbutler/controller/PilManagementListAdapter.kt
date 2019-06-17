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
import com.kau.smartbutler.model.Pill
import com.kau.smartbutler.model.PillCaution
import com.kau.smartbutler.view.main.home.child.DeviceModifiedActivity
import com.kau.smartbutler.view.main.life.child.PillDietActivity
import java.util.*

class PilManagementListAdapter (
        val mContext: Context,
        val modelList: ArrayList<Pill>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PillCautionListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.model_pill_management, parent, false))
    }

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val model = modelList.get(position)

        val holder = viewHolder as PillCautionListViewHolder

        holder.name.text = model.name
        holder.component.text = model.component
        holder.caution.text = model.caution
        holder.sideEffect.text = model.sideEffect
        holder.recommendSymptom.text = (model.recommendSymptom + "에 좋은 음식")
        holder.recommendFood.text = model.recommendFood
        holder.recommendDiet.text = (model.recommendSymptom + " 식단 추천")
        holder.recommendDiet.setOnClickListener {
            val i = Intent(mContext, PillDietActivity::class.java)
            i.putExtra("symptom", model.recommendSymptom)
            mContext.startActivity(i)
        }

    }


    class PillCautionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name:TextView
        var component:TextView
        var caution: TextView
        var sideEffect:TextView
        var recommendSymptom: TextView
        var recommendFood: TextView
        var recommendDiet: Button

        init {
            name = itemView.findViewById<TextView>(R.id.pill_name)
            component = itemView.findViewById<TextView>(R.id.pillComponent)
            caution = itemView.findViewById<TextView>(R.id.pillCaution)
            sideEffect = itemView.findViewById<TextView>(R.id.pillSideEffect)
            recommendSymptom = itemView.findViewById<TextView>(R.id.recommendSymptom)
            recommendFood = itemView.findViewById<TextView>(R.id.recommendFood)
            recommendDiet = itemView.findViewById<Button>(R.id.recommendDiet)
        }

    }
}