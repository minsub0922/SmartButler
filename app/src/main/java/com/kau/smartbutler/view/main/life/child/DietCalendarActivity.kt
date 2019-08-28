package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import android.util.Log
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.CalendarAdapter
import com.kau.smartbutler.model.CalendarItem
import com.kau.smartbutler.model.Diet
import com.kau.smartbutler.model.Meal
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_diet_calendar.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DietCalendarActivity(override val layoutRes: Int = R.layout.activity_diet_calendar, override val isUseDatabinding: Boolean=false) : BaseActivity(), View.OnClickListener{

    val calendarList:ArrayList<CalendarItem> = ArrayList()
    val type by lazy {
        intent.getStringExtra("type")
    }
    val adapter:CalendarAdapter by lazy { CalendarAdapter(this, calendarList, type) }
    var month :Int = 0
    var year: Int = 0

    private var realm = Realm.getDefaultInstance()

    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        setCalendarList()

        calendarRecyclerView.adapter = this.adapter

        adapter.notifyDataSetChanged()

        nextMonth.setOnClickListener(this)
        prevMonth.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.nextMonth -> {
                var cal = GregorianCalendar()
                var calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)
                month += 1
                if (month == 13) {
                    year += 1
                    month = 1
                }
                setCalendarList()
                adapter.notifyDataSetChanged()
            }
            R.id.prevMonth -> {
                month -= 1
                if(month == 0) {
                    year -= 1
                    month = 12
                }
                setCalendarList()
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setCalendarList() {

        val cal = GregorianCalendar() // 오늘 날짜
        calendarList.clear()
        try {
            var calendar = GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)
            if(month == 0) {
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
            }
            else {
                calendar = GregorianCalendar(year, month, 1, 0, 0, 0)
            }
            monthOfCalendarTextView.text = getMonthLetter(month) + " " + year.toString()
            //calendarList.add(calendar.timeInMillis.toString()) //날짜 타입

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
            val max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) //해당 월에 마지막 요일

            realm.beginTransaction()
            val dietListOfMonth = realm.where<Meal>()
                    .equalTo("year", calendar.get(Calendar.YEAR))
                    .equalTo("month", calendar.get(Calendar.MONTH))
                    .findAll()
            realm.commitTransaction()

            Log.d("test", dietListOfMonth.toString())
            val dateList = ArrayList<Int>()
            for (i in 0 .. 31)
                dateList.add(0)
            for (diet in dietListOfMonth) {
                dateList[diet.day.toInt()] = 1
            }
            Log.d("test", dateList.toString())


            for (j in 0 until dayOfWeek) {
                calendarList.add(CalendarItem(0,"", false))  //비어있는 일자 타입
            }
            for (j in 1 .. max) {
                if(cal.get(Calendar.YEAR) != calendar.get(Calendar.YEAR) || cal.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
                    calendarList.add(CalendarItem(0,j.toString(), false))
                } else {
                    if(j == cal.get(Calendar.DATE)) {
                        calendarList.add(CalendarItem(1,j.toString(), true))
                    }
                    else if (j > cal.get(Calendar.DATE)) {
                        calendarList.add(CalendarItem(0,j.toString(), false))
                    } else {
                        if(dateList[j] == 1)
                            calendarList.add(CalendarItem(0,j.toString(), true))
                        else
                            calendarList.add(CalendarItem(0,j.toString(), false))
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun getMonthLetter(month: Int): String {
        when(month) {
            0 -> return "Jan"
            1 -> return "Feb"
            2 -> return "Mar"
            3 -> return "Apr"
            4 -> return "May"
            5 -> return "Jun"
            6 -> return "Jul"
            7 -> return "Aug"
            8 -> return "Sep"
            9 -> return "Oct"
            10 -> return "Nov"
            11 -> return "Dec"
        }
        return "Jan"
    }



}