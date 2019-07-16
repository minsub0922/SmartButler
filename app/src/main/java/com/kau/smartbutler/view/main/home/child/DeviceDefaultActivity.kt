package com.kau.smartbutler.view.main.home.child
import android.util.Log
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.Device
import com.kau.smartbutler.util.network.GetHueRequest
import com.kau.smartbutler.util.network.getNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_device_etc.*

class DeviceDefaultActivity(override val layoutRes: Int = R.layout.activity_device_etc
                            , override val isUseDatabinding: Boolean = false) : BaseActivity() {

    override var isChildActivity: Boolean = true
    val minValue = 0
    val maxValue = 100
    var state = false
    lateinit var on: String
    lateinit var off: String
    val device by lazy { intent.getParcelableExtra("device") as Device }

    override fun setupView() {
        super.setupView()

        toolbarTitle.text = device.name

        getOrderType(device.stringType)

        deviceSwitch.setOnClickListener { OnOffListener() }
        powerButton.setOnClickListener { OnOffListener() }
    }

    private fun OnOffListener(){
        getNetworkInstance()
                .postOrder( device.path, if (state) off else on)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("tagg result ", it.toString())
                }

        state = !state
    }

    private fun getOrderType(deviceType: String){

        var pairValue: Pair<String, String> = Pair("","")

        when(deviceType){

            "1" -> pairValue = Pair("ON","OFF")
            "6" -> pairValue = Pair("vaccum","dock")
            "8" -> pairValue = Pair("PowerOn","PowerOff")

        }

        on = pairValue.first
        off = pairValue.second
    }

}