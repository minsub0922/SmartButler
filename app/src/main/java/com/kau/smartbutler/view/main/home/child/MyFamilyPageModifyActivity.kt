package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.FamilyInfomation
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_my_family_modify.*
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyFamilyPageModifyActivity(override val layoutRes: Int = R.layout.activity_my_family_modify, override val isUseDatabinding: Boolean = false) : BaseActivity(), View.OnClickListener{
    var realm = Realm.getDefaultInstance()
    var family_info: FamilyInfomation? = null

    override var isChildActivity: Boolean = true

    var family_name: String? = null
    var family_relation: String? = null
    var family_phone: String? = null
    var family_email: String? = null
    var family_address: String? = null

    override fun setupView() {
        super.setupView()

        myFamilyProfileSave.setOnClickListener(this)

        realm.beginTransaction()
        val initialInfo_list = realm.where<FamilyInfomation>().findAll()
        val initialInfo = initialInfo_list.last()
        realm.commitTransaction()

        if(initialInfo != null){
            family_nameEditText.setText(initialInfo.name)
            family_relationEditText.setText(initialInfo.relation)
            family_phoneEditText.setText(initialInfo.phone)
            family_emailEditText.setText(initialInfo.email)
            family_addressEditText.setText(initialInfo.address)
        }
    }

    // Save Button Click
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.myFamilyProfileSave -> {
                family_name = if(family_nameEditText.text.toString() == "") family_nameEditText.hint.toString() else family_nameEditText.text.toString()
                family_relation = if(family_relationEditText.text.toString() == "") family_relationEditText.hint.toString() else family_relationEditText.text.toString()
                family_phone = if(family_phoneEditText.text.toString() == "") family_phoneEditText.hint.toString() else family_phoneEditText.text.toString()
                family_email = if(family_emailEditText.text.toString() == "") family_emailEditText.hint.toString() else family_emailEditText.text.toString()
                family_address = if(family_addressEditText.text.toString() == "") family_addressEditText.hint.toString() else family_addressEditText.text.toString()

                if(is_valid()){
                    realm.beginTransaction()
                    family_info = FamilyInfomation(family_name!!, family_relation!!, family_phone!!, family_email!!, family_address!!)
                    realm.copyToRealm(family_info!!)
                    realm.commitTransaction()

                    Toast.makeText(this, "저장  완료",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MyPageActivity::class.java))
                    finish()
                }
            }
        }
    }

    fun is_valid(): Boolean {
        if (family_name.equals("") || family_name.equals(family_nameEditText.hint.toString()) ){
            Toast.makeText(this,"가족 이름을 입력하시오", Toast.LENGTH_LONG).show()
            return false
        }
        if (family_phone.equals("") || family_phone.equals(family_phoneEditText.hint.toString()) ){
            Toast.makeText(this, "전화번호를 입력하시오", Toast.LENGTH_LONG).show()
            return false
        }
        else{
            return true
        }
    }
}