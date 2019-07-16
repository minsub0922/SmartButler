package com.kau.smartbutler.view.main.home.child
import android.util.Log
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.Device
import com.kau.smartbutler.util.network.GetHueRequest
import com.kau.smartbutler.util.network.getNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_device_light.*

class DeviceLightActivity(override val layoutRes: Int = R.layout.activity_device_light
                          , override val isUseDatabinding: Boolean = false) : BaseActivity() {

    override var isChildActivity: Boolean = true
    val defaultValue = "338,100,"
    val minValue = 0
    val maxValue = 100
    var state = 0
    val device by lazy { intent.getParcelableExtra("device") as Device }

    override fun setupView() {
        super.setupView()

        lightUpButton.setOnClickListener { v  ->

            if (maxValue <= lightSeekbar.progress) return@setOnClickListener
            lightSeekbar.progress = lightSeekbar.progress + 20

            getNetworkInstance()
                    .postOrder( device.path, defaultValue+lightSeekbar.progress.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("tagg result ", it.toString())
                    }

        }

        lightDownButton.setOnClickListener{v ->

            if (minValue >= lightSeekbar.progress) return@setOnClickListener
            lightSeekbar.progress = lightSeekbar.progress - 20

            getNetworkInstance()
                    .postOrder( device.path, defaultValue+lightSeekbar.progress.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.d("tagg result ", it.toString())
                    }
        }
    }

}