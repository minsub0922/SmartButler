package com.kau.smartbutler.view.main.life.child

import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.MyReservationControllerAdapter
import com.kau.smartbutler.controller.ReservationSearchControllerAdapter
import com.kau.smartbutler.model.Hospital
import kotlinx.android.synthetic.main.activity_reservation.*

class ReservationActivity(
        override val layoutRes: Int= R.layout.activity_reservation,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity() {
    override var isChildActivity: Boolean = true
    val reservationList = ArrayList<Hospital>()
    val searchList = ArrayList<Hospital>()
    val reservationAdapter by lazy { MyReservationControllerAdapter(this, reservationList) }
    val searchAdapter by lazy { ReservationSearchControllerAdapter(this, searchList)}

    override fun setupView() {
        super.setupView()

        hospital_searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if(hasFocus) {
                reservationRecyclerView.adapter = searchAdapter
            } else {
                reservationRecyclerView.adapter = reservationAdapter
            }
        }

        reservationRecyclerView.adapter = reservationAdapter
        setDeviceList()

    }

    private fun setDeviceList(){

        reservationList.add(Hospital(0, "연대 세브란스 병원", "서울 서대문구 연세로 50-1", "02-333-4456"))
        reservationList.add(Hospital(0, "서대문 치과", "서울 서대문구 연세로 50-1", "02-333-4456"))
        reservationList.add(Hospital(1, "하늘 택시", "서울 서대문구 연세로 50-1", "02-333-4456"))
        reservationAdapter.notifyDataSetChanged()

        searchList.add(Hospital(0, "한을 남하늘 피부과 의원", "서울특별시 서대문구 성산로 317 하늘라운지 2층", "02-002-0002"))
        searchList.add(Hospital(0, "하늘하늘 의원", "서울특별시 서대문구 수색로 321-12", "02-002-0002"))
        searchAdapter.notifyDataSetChanged()

    }

}
