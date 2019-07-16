package com.kau.smartbutler.view.main.home.child

import android.util.Log
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.Device
import com.kau.smartbutler.util.network.getNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_device_tv.*


class DeviceTVActivity(override val layoutRes: Int = R.layout.activity_device_tv
                       , override val isUseDatabinding: Boolean = false) : BaseActivity() {

    override var isChildActivity: Boolean = true
    private var tvState: Boolean = false
    val device by lazy { intent.getParcelableExtra("device") as Device }
    override fun setupView() {
        super.setupView()

        powerButton.setOnClickListener{
            getNetworkInstance()
                    .postOrder( device.path,if (tvState) "PowerOff" else "PowerOn")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("tagg result ", it.toString())
                    }
            tvState = !tvState
        }

    }

}