package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.PersonalInformation
import com.kau.smartbutler.model.PostCctvIpRequest
import com.kau.smartbutler.model.Profile
import com.kau.smartbutler.util.network.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_my_profile_modify.*

class MyPageModifyActivity(override val layoutRes: Int = R.layout.activity_my_profile_modify, override val isUseDatabinding: Boolean = false) : BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    var realm = Realm.getDefaultInstance()
    var info: PersonalInformation? = null

    var age: Int? = null
    var sex: String? = null
    var weight: Int? = null
    var goalWeight: Int? = null
    var activity:String? = null

    override fun setupView() {
        super.setupView()

        myProfileModify.setOnClickListener(this)
        realm.beginTransaction()
        val initialInfo = realm.where<PersonalInformation>().findFirst()
        realm.commitTransaction()
        if (initialInfo != null) {
            spinnerSex.setSelection(getSexPosition(initialInfo.sex))
            spinnerActivity.setSelection(getActivityPosition(initialInfo.activity))
            et_age.setText(initialInfo.age.toString())
            et_weight.setText(initialInfo.weight.toString())
            et_goal_weight.setText(initialInfo.goalWeight.toString())
        }

        val adapterSex = ArrayAdapter.createFromResource(this, R.array.sex, R.layout.my_profile_spinner_item)
        spinnerSex.adapter = adapterSex
        spinnerSex.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sex = spinnerSex.getItemAtPosition(p2).toString()
            }
        }

        val adapterActivity = ArrayAdapter.createFromResource(this, R.array.activity, R.layout.my_profile_spinner_item)
        spinnerActivity.adapter = adapterActivity
        spinnerActivity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                activity = spinnerActivity.getItemAtPosition(p2).toString()
            }
        }
        realm.beginTransaction()
        val initialProfile = realm.where<Profile>().findFirst()
        realm.commitTransaction()
        if (initialProfile != null) {
            phoneEditText.setText(initialProfile.phone)
            emailEditText.setText(initialProfile.email)
            addressEditText.setText(initialProfile.address)
            cctvIP.setText(initialProfile.cctvIP)
            openhabIP.setText(initialProfile.openhapIP)
            serverIP.setText(initialProfile.serverIP)
            serverPort.setText(initialProfile.serverPort)
        }


    }

    override fun onClick(v: View?) {
        var server_ip = ""
        var server_port = ""
        var cctv_ip = ""
        when(v!!.id){
            R.id.myProfileModify -> {
                age = if(et_age.text.toString() == "") 30 else et_age.text.toString().toInt()
                weight = if(et_weight.text.toString() == "") 70 else et_weight.text.toString().toInt()
                goalWeight = if(et_goal_weight.text.toString() == "") 70 else et_goal_weight.text.toString().toInt()
                realm.beginTransaction()
                info = PersonalInformation(age!!, sex!!, weight!!, goalWeight!!, activity!!)
                realm.copyToRealm(info!!)
                realm.commitTransaction()

                getListNetworkInstance()
                        .getDailyCalorieRequirements(weight!!, age!!, sex!!, activity!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            realm.beginTransaction()
                            val test = realm.where(PersonalInformation::class.java).findFirstAsync()
                            test.requiredCalorie = it.get("dailyCalorieRequirements").toString().toInt()
                            realm.commitTransaction()

                            Log.d("tag result ", it.get("dailyCalorieRequirements").toString() + ", " + test.requiredCalorie.toString())
                            Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                        },{
                            Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show()
                        })

                val phone = if(phoneEditText.text.toString() == "") "010-0000-0000" else phoneEditText.text.toString()
                val email = if(emailEditText.text.toString() == "") "example@example.com" else emailEditText.text.toString()
                val address = if(addressEditText.text.toString() == "") "서울시 서대문구" else addressEditText.text.toString()
                val cctvIP = if(cctvIP.text.toString() == "") "172.16.16.87" else cctvIP.text.toString()
                val openhabIP = if(openhabIP.text.toString() == "" ) "0.0.0.0" else openhabIP.text.toString()
                val serverIP = if(serverIP.text.toString() == "") "0.0.0.0" else serverIP.text.toString()
                val serverPort = if(serverPort.text.toString() == "") "10001" else serverPort.text.toString()
                server_ip = serverIP
                server_port = serverPort
                cctv_ip = cctvIP
                realm.beginTransaction()
                val profile = Profile("홍길동", phone, email, address, cctvIP, openhabIP, serverIP, serverPort)

                val existData = realm.where(Profile::class.java).findFirstAsync()
                val allData = realm.where<Profile>().findAll()
                realm.copyToRealm(profile)
                if(allData.size>1)
                    existData.deleteFromRealm()     // copyToRealm 사용방법 물어보기 -> 일단 데이터가 2개일 경우 기존 데이터를 삭제하는 식으로 진행.
                realm.commitTransaction()

                API_BASE_URL = "http://$openhabIP:8080"

            }
        }
        postCctvIp(server_ip, server_port, cctv_ip)
    }

    fun postCctvIp(serverIP: String, serverPORT: String, cctvIP: String) {

        CCTV_ANALYSIS_URL = "http://$serverIP:$serverPORT"
        networkInit()

        getCCTVNetworkInstance()
                .postCctvIp(PostCctvIpRequest(cctvIP))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    Log.d("CCTV_IP Setting Result:", it.toString())
                },
                        {
                            error->
                        })

    }

    fun getSexPosition(sex: String): Int {
        when(sex) {
            "남" -> { return 0}
            "여" -> { return 1}
        }
        return -1
    }

    fun getActivityPosition(activity: String): Int {
        when(activity) {
            "하" -> { return 0}
            "중" -> { return 1}
            "상" -> { return 2}
        }
        return -1
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}