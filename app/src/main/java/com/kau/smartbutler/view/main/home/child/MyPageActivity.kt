package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.FamilyInfomation
import io.realm.FamilyInfomationRealmProxy
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyPageActivity(override val layoutRes: Int = R.layout.activity_my_profile, override val isUseDatabinding: Boolean = false) : BaseActivity() {
    override var isChildActivity: Boolean = true

    lateinit var realm: Realm

    override fun setupView() {
        super.setupView()

        Realm.init(this)
        realm = Realm.getDefaultInstance()

        getFamilyInfo()
        realm.close()


        profileModifyButton.setOnClickListener{
            startActivity(Intent(this, MyPageModifyActivity::class.java))
            finish()
        }

        profileGroup.setOnClickListener{
            startActivity(Intent(this, MyFamilyPageModifyActivity::class.java))
            finish()
        }
    }

    private fun getFamilyInfo() {
        var family_info_list = realm.where<FamilyInfomation>().findAll()
        var family_info = family_info_list.last()

        if (family_info != null){
            val family_name = (family_info as FamilyInfomationRealmProxy).`realmGet$name`()
            val family_relation = (family_info as FamilyInfomationRealmProxy).`realmGet$relation`()
            familyMembersTextView.setText("$family_name/$family_relation")
        }
    }
}