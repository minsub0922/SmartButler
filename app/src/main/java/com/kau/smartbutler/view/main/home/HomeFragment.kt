package com.kau.smartbutler.view.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.ObservableField
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment
import com.kau.smartbutler.controller.DeviceControllerAdpater
import com.kau.smartbutler.model.Device
import com.kau.smartbutler.util.network.getNetworkInstance
import com.kau.smartbutler.util.network.getNetworkInstanceForJson
import kotlinx.android.synthetic.main.fragment_home.*
import com.kau.smartbutler.util.recylcerview.GridSpacingItemDecoration
import com.kau.smartbutler.view.main.home.child.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home_state.*
import org.json.JSONObject

class HomeFragment : BaseFragment() , View.OnClickListener, DeviceControllerAdpater.DeviceControllerItemClickListener{

    override fun onClick(v: View?) {
        if (v!!.id == R.id.btn_temperature){
            startActivity(Intent(activity, TemperatureHumidityActivity::class.java))
        }
    }

    override val layoutRes: Int = R.layout.fragment_home
    val modelList = DeviceListSingleton.getInstance().list
    val adapter by lazy {
        DeviceControllerAdpater(activity!!, modelList, false, this)
    }
    var updateDeviceState = false

    companion object {
        var INSTANCE: HomeFragment? = null

        fun getInstance(): HomeFragment {
            if (INSTANCE == null)
                INSTANCE = HomeFragment()
            return INSTANCE!!
        }

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onResume() {
        super.onResume()

        if (updateDeviceState) {
            adapter.notifyDataSetChanged()
            updateDeviceState = false
        }
    }

    fun updateDeviceList(){
        updateDeviceState = true
    }

    override fun setupView(view: View) {

        setModels()

        rv_home_control.adapter = adapter
        rv_home_control.addItemDecoration(GridSpacingItemDecoration(3, 40, false))

        refreshOFF()

        btn_device_switch.setOnClickListener { v->
            adapter.toggleSwitch = !adapter.toggleSwitch
            adapter.notifyDataSetChanged()
        }

        btn_temperature.setOnClickListener(this)

    }

    override fun OnDeiveClicked(device: Device) {

        val intent = when(device.type){

            0 -> Intent(activity, DeviceListActivity::class.java)
            1 -> Intent(activity, DeviceLightActivity::class.java)
            2 -> Intent(activity, DeviceTVActivity::class.java)
            3 -> Intent(activity, DeviceAirconActivity::class.java)
            else -> Intent(activity, DeviceDefaultActivity::class.java)
        }

        intent.putExtra("device", device)
        startActivity(intent)
    }

    private fun setModels(){

        getNetworkInstanceForJson().getDeviceNames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({res ->

                    res.forEach{
                        try {
                            val str = it.asJsonObject.get("label").toString()
                            val link = it.asJsonObject.get("channels").asJsonArray.get(0).asJsonObject.get("linkedItems")
                            val idx = str.indexOf("_")

                            if (idx > 0 ) { //is registered device

                                val name = str.substring(idx+1 until str.length-1)

                                when(val stringType = str.substring(1 until idx)){
                                    //deviceTypes
                                    "71","72","73" -> modelList.add(Device(1, name = name, path = link.toString(), stringType = stringType))
                                    "10" -> modelList.add(Device(3, name = name, path = link.toString(), stringType = stringType))
                                    "11" -> modelList.add(Device(2, name = name, path = link.toString(), stringType = stringType))
                                    "1","6","8" -> modelList.add(Device(4, name = name, path = link.toString(), stringType = stringType))
                                }
                            }
                        }catch (ex: Exception){
                            Log.d("tagg error!", ex.toString())
                        }
                    }

                    adapter.notifyDataSetChanged()

                },{})

    }
}