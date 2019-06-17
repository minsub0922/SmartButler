package com.kau.smartbutler.view.main.home.child

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.kau.smartbutler.R
import kotlinx.android.synthetic.main.activity_temperature_humidity_dialog.*


class TemperatureHumidityDialog(private val context: Context, val reservationInterface: SetReservation){

    lateinit var buttonList: Array<Button>
    var endReserv = false
    var startReserv = false
    var temp = 0
    var reservTime = ""

    val dlg by lazy {
       val dlg =  Dialog(context)
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.activity_temperature_humidity_dialog)
        return@lazy dlg
    }

    interface SetReservation{
        fun onCompleted( endReserv: Boolean, startReserv: Boolean, temp: Int, reservTime: String )
    }

    fun callFunction() {

        dlg.apply {

            buttonList = arrayOf(reserveTimeButton1, reserveTimeButton2,reserveTimeButton3, reserveTimeButton4)

            for (button:Button in buttonList) button.setOnClickListener{ v -> OnClickListener(v)}
            reserveDoneButton.setOnClickListener{ v -> OnClickListener(v)}
            btn_close.setOnClickListener{ v-> OnClickListener(v)}
            reserveCloseSwitch.setOnClickListener{ v-> OnClickListener(v)}
            reserveStartSwitch.setOnClickListener{ v-> OnClickListener(v)}

            show()

        }
    }

    private fun OnClickListener(v: View){

        if (v.id == R.id.reserveDoneButton ) {
            reservationInterface.onCompleted(endReserv, startReserv, temp, reservTime)
            dlg.dismiss()
            return
        }
         if (v.id == R.id.reserveCloseSwitch){
            endReserv = (v as SwitchCompat).isChecked
        }
        else if (v.id == R.id.reserveStartSwitch){
            startReserv= (v as SwitchCompat).isChecked
        }
        else if (v.id == R.id.btn_close) dlg.dismiss()
        else {
            for (b : Button in buttonList) {
                if (b.id != v.id) b.isSelected = false
                else {
                    v.isSelected = true
                    reservTime = (v as Button).text.toString()
                }
            }

        }

    }

}
