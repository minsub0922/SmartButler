package com.kau.smartbutler.view.main.home.child

import android.util.Log
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import android.view.View
import com.kau.smartbutler.controller.SpinnerItemAdapter
import kotlinx.android.synthetic.main.activity_temerature_humidity.*


class TemperatureHumidityActivity : BaseActivity(), SpinnerItemAdapter.ItemClickListener, TemperatureHumidityDialog.SetReservation{

    override val isUseDatabinding: Boolean = false
    override val layoutRes: Int = R.layout.activity_temerature_humidity
    val strings = arrayOf("0% ~ 20%", "30% ~ 50% (권장습도)", "60% ~ 80%")
    var selectedHumidity = 0
    override var isChildActivity = true
    val adapter: SpinnerItemAdapter by lazy { SpinnerItemAdapter(this, strings,0, this) }

    override fun setupView() {
        super.setupView()

        setSpinner()

        spinnerItemRecyclerView.adapter = adapter

        humidTextView.text = trimDesciption(strings[selectedHumidity]).trim()

        setReserveSettings()

        toolbarTitle.text = "온도 및 습도"

    }

    private fun setSpinner(){

        spinnerButton.setOnClickListener {

            spinnerButton.isSelected = !spinnerButton.isSelected
            spinnerItemRecyclerView.visibility = if (spinnerButton.isSelected) View.VISIBLE else View.INVISIBLE
            if (!spinnerButton.isSelected) {
                adapter.selectedPosition = selectedHumidity
                adapter.notifyDataSetChanged()
            }

        }

    }

    private fun setReserveSettings(){

        settingButton.setOnClickListener{ TemperatureHumidityDialog(this, this).callFunction() }
    }

    override fun onCompleted(endReserv: Boolean, startReserv: Boolean, temp: Int, reservTime: String) {
        if (!endReserv&&!startReserv) switch_reserve.isChecked = false
        else if (endReserv) reserveTypeTextView.text = "꺼짐 예약"
        else if (startReserv) reserveTypeTextView.text = "켜짐 예약"

        reserveTimeTextView.text = reservTime

    }

    override fun onClick(pos: Int) {
        if (pos == adapter.selectedPosition) {
            spinnerItemRecyclerView.visibility = View.INVISIBLE
            spinnerButton.isSelected = false
            humidTextView.text = trimDesciption(strings[pos]).trim()
            selectedHumidity = pos
        }
        adapter.selectedPosition = pos
        adapter.notifyDataSetChanged()

    }

    private fun trimDesciption(str: String): String{

        val idx = str.indexOf("(")
        if (idx < 0) return str
        return str.substring(0, idx-1)

    }


}