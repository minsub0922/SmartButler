package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.CalendarAdapter
import com.kau.smartbutler.model.CalendarItem
import kotlinx.android.synthetic.main.activity_diet_calendar.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DietCalendarActivity(override val layoutRes: Int = R.layout.activity_diet_calendar, override val isUseDatabinding: Boolean=false) : BaseActivity(){

    val calendarList:ArrayList<CalendarItem> = ArrayList()
    val type by lazy {
        intent.getStringExtra("type")
    }
    val adapter:CalendarAdapter by lazy { CalendarAdapter(this, calendarList, type) }

    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        setCalendarList()

        calendarRecyclerView.adapter = this.adapter

        adapter.notifyDataSetChanged()

    }

    fun setCalendarList() {

        val cal = GregorianCalendar() // 오늘 날짜

        try {
            val calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)

            //calendarList.add(calendar.timeInMillis.toString()) //날짜 타입

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
            val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) //해당 월에 마지막 요일

            for (j in 0 until dayOfWeek) {
                calendarList.add(CalendarItem(0,"", false))  //비어있는 일자 타입
            }
            for (j in 1 .. max) {
                calendarList.add(CalendarItem(0,j.toString(), j %2 == 0))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



}