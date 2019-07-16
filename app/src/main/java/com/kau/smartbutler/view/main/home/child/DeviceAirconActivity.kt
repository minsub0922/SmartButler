package com.kau.smartbutler.view.main.home.child

import android.util.Log
import android.view.View
import android.widget.Button
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.Device
import com.kau.smartbutler.util.network.getNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_device_aircon.*

class DeviceAirconActivity(
        override val layoutRes: Int = R.layout.activity_device_aircon,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity(), View.OnClickListener{

    override var isChildActivity: Boolean = true
    val device by lazy { intent.getParcelableExtra("device") as Device }
    val stateButtonArray by lazy {
        arrayOf(stateButton1, stateButton2, stateButton3, stateButton4)
    }

    var airconState = false

    override fun setupView() {
        super.setupView()

        powerButton.isSelected = true

        for (b in stateButtonArray) b.setOnClickListener(this)

        powerButton.setOnClickListener{

            getNetworkInstance()
                    .postOrder( device.path,if (airconState) "PowerOff" else "PowerOn")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("tagg result ", it.toString())
                    }
            airconState = !airconState

        }

    }

    override fun onClick(v: View?) {
        for (b in stateButtonArray) b.isSelected = b.id == v!!.id
    }


}