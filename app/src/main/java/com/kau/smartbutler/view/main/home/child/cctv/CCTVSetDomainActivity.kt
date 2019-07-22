package com.kau.smartbutler.view.main.home.child.cctv
//현문제. 화면크기를 imageview에 맞추면 된다리. 버튼 누를때 ontouch와 imageview의 톱 위치를 맞게 설정
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cctv_set_domain.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CCTVSetDomainActivity(
        override val layoutRes : Int = R.layout.activity_cctv_set_domain,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {
    var mPaint = Paint()
    //터치 한 곳의 좌표를 저장할 변수
    internal var x = -1f
    internal var y = -1f
    //터치했던곳의 좌표 배열
    internal var vy = ArrayList<Float>()
    internal var vx = ArrayList<Float>()

    //상대좌표
    internal var relative_coordinate_X = -1f
    internal var relative_coordinate_Y = -1f

    //상대좌표 list
    internal var relative_coordinateX_V = ArrayList<Float>()
    internal var relative_coordinateY_V = ArrayList<Float>()

    //좌표를 ArrayList에 저장할지 설정 ( 1 받음 0 안받음)
    internal var input_flag = 1
    // 이전단계의 터치 좌표
    private var start_x = -1f
    private var start_y = -1f
    //경로 설정 변수(이거대신 일단 drawLine씀)
    private val path: Path? = null
    /////////////////////////////////////////////////////////////////////////////
    internal var sObject = JSONObject()

    internal var coordinates = java.util.ArrayList<java.util.ArrayList<*>>()
    internal var coordinates_full = java.util.ArrayList<java.util.ArrayList<*>>()



    ////////////////////////////////////////////////////////////////////////////////////////////////////
//onDrawfunction
    internal fun onDraw(canvas :Canvas) {
        mPaint.setColor(Color.GREEN)
        mPaint.setStrokeWidth(5F)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setAntiAlias(true)
        mPaint.setDither(true)

        if (x > 0 && y > 0) {
            start_x = x
            start_y = y
            for (i in 0 until vx.size) {
                if (i == 0) {
                    canvas.drawCircle(vx[0], vy[0], 5f, mPaint)
                }
                if (i > 0) {
                    start_x = vx[i - 1]
                    start_y = vy[i - 1]
                    x = vx[i]
                    y = vy[i]
                    if (Math.abs(vx[0] - x) < 100 && Math.abs(vy[0] - y) < 100) {
                        input_flag = 0//더이상 입력을 받지 않음
                        canvas.drawLine(start_x, start_y, vx[0], vy[0], mPaint)
                        break
                    }
                    canvas.drawCircle(vx[i], vy[i], 10f, mPaint)
                    canvas.drawLine(start_x, start_y, x, y, mPaint)
                }
            }
            start_x = x
            start_y = y
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    fun initJson() {
        val arr = JSONArray()

        try {
            sObject.put("Intrusion", arr)
            sObject.put("Loitering", arr)
            sObject.put("Abandon", arr)
            sObject.put("Falldown", arr)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun set_detect_area_event(event_name: String, is_reset: Boolean) {
        try {
            sObject.remove(event_name)
            if (is_reset)
                sObject.put(event_name, "[]")
            else if (event_name == "Falldown") {
                sObject.put(event_name, coordinates_full)
            } else {
                sObject.put(event_name, coordinates)
            }
            Log.d("[INFO] coordinates : ", coordinates.toString())
        } catch (e: JSONException) {
            Log.d("[INFO] ERROR : ", e.toString())
        }
    }





    override var isChildActivity: Boolean = true
    @SuppressLint("ClickableViewAccessibility")
    override fun setupView() {
        super.setupView()
        val iv = findViewById(R.id.cctv_image) as ImageView
        val arr = intent.getByteArrayExtra("image")
        val bm = BitmapFactory.decodeByteArray(arr, 0, arr.size)
        val resized = Bitmap.createScaledBitmap(bm,1080 , 1170, true)
        var canvas =Canvas()
        iv.setImageBitmap(resized)
        initJson()
        //한번실행 full 좌표
//        for (i in 0..3) {
//            val points = java.util.ArrayList<Int>()
//            if (i == 0) {
//                points.add(0)
//                points.add(0)
//            } else if (i == 1) {
//                points.add(iv.getWidth())
//                points.add(0)
//            } else if (i == 2) {
//                points.add(iv.getWidth())
//                points.add(iv.getHeight())
//            } else {
//                points.add(0)
//                points.add(iv.getHeight())
//            }
//            coordinates_full.add(points)
//        }

//패인트 색깔
        mPaint.setColor(Color.BLUE)
        mPaint.setStrokeWidth(10F)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setAntiAlias(true)
        mPaint.setDither(true)
////////////////////////////////////////////////////////////////////////////////////////////////////
//will use variable
        canvas.setBitmap(resized)
        Log.d("myapp", "AAA2: " + iv.width)
        Log.d("myapp", "AAA2: "+ iv.height)

        iv.setOnTouchListener(){ view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val points = ArrayList<Float>()// 임시 좌표비
//                    Log.d("myapp", "AAA3: " + iv.width)
//                    Log.d("myapp", "AAA3: "+ iv.height)
                    x = motionEvent.getX()
                    y = motionEvent.getY()
                    relative_coordinate_X = x/1080f
                    relative_coordinate_Y = y/1170f

                    //좌표벡터에 넣어준다.
                    if (input_flag == 1) {
                        vx.add(x)
                        vy.add(y)
                        points.add(relative_coordinate_X)//현좌표비를 넣는다
                        points.add(relative_coordinate_Y)//현좌표비를 넣는다.
                        coordinates.add(points) //최종 좌표비 (x비,y비) 변수에 넣는다.
                        Log.d("sedo", "coordintessize : "+ coordinates.size)
                        Log.d("sedo", "현좌표X : "+ coordinates[coordinates.size-1][0])
                        Log.d("sedo", "현좌표Y : "+ coordinates[coordinates.size-1][1])
                    }
                }
            }
            onDraw(canvas)
            iv.invalidate()
            return@setOnTouchListener true
        }


////////////////////////////////////////////////////////////////////////////////////////////////////
        //button event
        stateButton1.setOnClickListener {
            if (stateButton1.isChecked()) {
                Log.d("Intrusion", "Enable")
                set_detect_area_event("Intrusion", false)
            } else {
                Log.d("Intrusion", "Disable")
                set_detect_area_event("Intrusion", true)
            }
        }

        stateButton2.setOnClickListener {
            if (stateButton2.isChecked()) {
                Log.d("Loitering", "Enable")
                set_detect_area_event("Loitering", false)
            } else {
                Log.d("Loitering", "Disable")
                set_detect_area_event("Loitering", true)
            }
        }

        stateButton3.setOnClickListener {
            if (stateButton3.isChecked()) {
                Log.d("Abandon", "Enable")
                set_detect_area_event("Abandon", false)
            } else {
                Log.d("Abandon", "Disable")
                set_detect_area_event("Abandon", true)
            }
        }

        stateButton4.setOnClickListener {
            if (stateButton4.isChecked()) {
                Log.d("Falldown", "Enable")
                set_detect_area_event("Falldown", false)
            } else {
                Log.d("Falldown", "Disable")
                set_detect_area_event("Falldown", true)
            }
        }
    }
    ///?S?D?FA?
}
