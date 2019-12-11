package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.FamilyInfomation
import com.kau.smartbutler.model.PersonalInformation
import com.kau.smartbutler.model.Profile
import io.realm.FamilyInfomationRealmProxy
import io.realm.PersonalInformationRealmProxy
import io.realm.ProfileRealmProxy
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

        getProfileInfo()
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

    private fun getProfileInfo() {
        var personal_info_list = realm.where<PersonalInformation>().findAll()
        if (personal_info_list.size != 0){
            var personal_info = personal_info_list.last()
            if (personal_info != null) {
                val personal_age = (personal_info as PersonalInformationRealmProxy).`realmGet$age`()
                ageTextView.setText("($personal_age 세)")
            }
        }


        var profile_info_list = realm.where<Profile>().findAll()
        if (profile_info_list.size != 0) {
            var profile_info = profile_info_list.last()
            if (profile_info != null) {
                val profile_name = (profile_info as ProfileRealmProxy).`realmGet$name`()
                nameTextView.setText(profile_name)
            }
        }
    }

    private fun getFamilyInfo() {
        var family_info_list = realm.where<FamilyInfomation>().findAll()
        if (family_info_list.size != 0) {
            var family_info = family_info_list.last()

            if (family_info != null) {
                val family_name = (family_info as FamilyInfomationRealmProxy).`realmGet$name`()
                val family_relation = (family_info as FamilyInfomationRealmProxy).`realmGet$relation`()
                familyMembersTextView.setText("$family_name/$family_relation")
            }
        }
    }
}