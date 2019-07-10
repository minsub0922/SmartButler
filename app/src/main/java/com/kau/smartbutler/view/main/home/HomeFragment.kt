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
        Log.d("tagg","resume!!")

        if (updateDeviceState) {
            Log.d("tagg","in,.,.?")
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

        refrestOFF()

        btn_device_switch.setOnClickListener { v->

            adapter.toggleSwitch = !adapter.toggleSwitch

            adapter.notifyDataSetChanged()

        }

        btn_temperature.setOnClickListener(this)

    }

    override fun OnDeiveClicked(device: Device) {
        when(device.type){

            0 -> startActivity(Intent(activity, DeviceListActivity::class.java))
            1 -> startActivity(Intent(activity, DeviceLightActivity::class.java))
            2 -> startActivity(Intent(activity, DeviceTVActivity::class.java))
            3 -> startActivity(Intent(activity, DeviceAirconActivity::class.java))
        }
    }

    private fun setModels(){

        getNetworkInstanceForJson().getDeviceNames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({res ->

                    Log.d("tagg : ,", res[0].toString())
//                    try {
//
//                        val obj = JSONObject(res)
//
//                        Log.d("tagg : ", obj.toString())
//
//                    } catch (t: Throwable) {
//                        Log.e("tagg : ", "Could not parse malformed JSON: \"$res\"")
//                    }
                    res.forEach{
                        try {
                            val str = it.asJsonObject.get("label").toString()
                            val link = it.asJsonObject.get("channels").asJsonArray.get(0).asJsonObject.get("linkedItems")
                            val idx = str.indexOf("_")

                            if (idx > 0 ) { //is registered device

                                val name = str.substring(idx+1 until str.length-1)

                                Log.d("tagg ","id : ${str.substring(0 until idx)}, name : ${name}, path : ${link}")

                                when(str.substring(1 until idx)){

                                    "71","72","73" -> modelList.add(Device(1, name = name, path = link.toString()))
                                    "10" -> modelList.add(Device(3, name = name, path = link.toString()))
                                    "11" -> modelList.add(Device(2, name = name, path = link.toString()))
                                    "1","6","8" -> modelList.add(Device(4, name = name, path = link.toString()))

                                }

                            }
                        }catch (ex: Exception){
                            Log.d("tagg error!", ex.toString())
                        }




                    }

                    adapter.notifyDataSetChanged()


                },{

                })



        //modelList.add(Device(0,""))
//        modelList.add(Device(1,"서재 조명"))
//        modelList.add(Device(2,"안방 에어컨"))
//        modelList.add(Device(3,"침실 조명"))
//        modelList.add(Device(1,"안방 tv"))
//        modelList.add(Device(2,"거실 tv"))
//        modelList.add(Device(1,"거실 조명"))
//        modelList.add(Device(1,"서재 조명"))
//        modelList.add(Device(2,"안방 에어컨"))
//        modelList.add(Device(3,"침실 조명"))
//        modelList.add(Device(1,"안방 tv"))
//        modelList.add(Device(2,"거실 tv"))
//        modelList.add(Device(1,"거실 조명"))


    }
}