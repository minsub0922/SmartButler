package com.kau.smartbutler.view.main.home.child.cctv

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.CCTV
import com.kau.smartbutler.model.PostDetectionAreaRequest
import com.kau.smartbutler.util.network.getCCTVNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_cctv_set_domain.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CCTVSetDomainActivity(
        override val layoutRes: Int = R.layout.activity_cctv_set_domain,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {
    var mPaint = Paint()

    // 현재 좌표
    internal var x = -1f
    internal var y = -1f
    // 현재로부터 -1번째 좌표
    private var start_x = -1f
    private var start_y = -1f

    // 좌표 저장 배열
    internal var vy = ArrayList<Float>()
    internal var vx = ArrayList<Float>()
    //좌표의 저장 여부 ( 1 받음 0 안받음 )
    internal var input_flag = 1

    lateinit var realm: Realm   //realm 초기화
    internal var sObject = JSONObject()     //json 초기화
    internal var coordinates = java.util.ArrayList<java.util.ArrayList<*>>()    //모든 좌표 저장
    var coordinates_full = java.util.ArrayList<java.util.ArrayList<*>>()    //모든 좌표 저장(화면 최대치로)
    internal var relative_coordinates = java.util.ArrayList<java.util.ArrayList<*>>()      // 모든 상대좌표 저장(소숫점 2자리까지)
    var relative_coordinates_full = java.util.ArrayList<java.util.ArrayList<*>>()       //모든 상대좌표 저장(화면 최대치로)
    internal var jsonstate = arrayOf(false, false, false, false)

    companion object {
        var vx = java.util.ArrayList<Float>()
        var vy = java.util.ArrayList<Float>()
        var relative_vx = java.util.ArrayList<Float>()
        var relative_vy = java.util.ArrayList<Float>()
    }

    //realm의 primary Key를 1부터 1씩 증가시키기 위한 함수.
    private fun nextId(): Int {
        val maxId = realm.where<CCTVRealmStruct>().max("id")// where 테이블 모든 값 얻기, 이메소드는 RealmQuery 객체를 반환, max는 가장 큰 값 얻음
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0
    }

    internal fun onDraw(canvas: Canvas) {
        mPaint.setColor(Color.GREEN)
        mPaint.setStrokeWidth(5f)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setAntiAlias(true)
        mPaint.setDither(true)

        // 선 그리기 부분
        if (x > 0 && y > 0) {
            start_x = x
            start_y = y
            for (i in 0 until CCTVSetDomainActivity.vx.size) {
                if (i == 0) {
                    canvas.drawCircle(CCTVSetDomainActivity.vx[0], CCTVSetDomainActivity.vy[0], 10f, mPaint!!)  //첫번째 클릭시 원 생성
                }

                if (i > 0) {
                    start_x = CCTVSetDomainActivity.vx[i - 1]
                    start_y = CCTVSetDomainActivity.vy[i - 1]
                    x = CCTVSetDomainActivity.vx[i]
                    y = CCTVSetDomainActivity.vy[i]

                    //현재 터치한 곳이 처음 터치한 좌표 부근이면, 헌재터치 이전의 좌표와 처음 터치한 좌표를 이어줌.
                    if (Math.abs(CCTVSetDomainActivity.vx[0] - x) < 100 && Math.abs(CCTVSetDomainActivity.vy[0] - y) < 100) {
                        input_flag = 0//더이상 입력을 받지 않음
                        canvas.drawLine(start_x, start_y, CCTVSetDomainActivity.vx[0], CCTVSetDomainActivity.vy[0], mPaint!!)
                        break
                    }
                    canvas.drawCircle(CCTVSetDomainActivity.vx[i], CCTVSetDomainActivity.vy[i], 10f, mPaint!!)  //터치한 부분에 원 생성
                    canvas.drawLine(start_x, start_y, x, y, mPaint!!)       //좌표간 선으로 잇기
                }
            }
            start_x = x
            start_y = y
        }
    }

    //Json 초기화
    fun initJson() {
        val arr = JSONArray()

        try {
            sObject.put("Intrusion", "[]")
            sObject.put("Loitering", "[]")
            sObject.put("Abandon", "[]")
            sObject.put("Falldown", "[]")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    //함수로 안쓰고 일단 직접 put하는 방식으로 하였음. 나중에 변경 가능.
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
        Realm.init(this)
        realm = Realm.getDefaultInstance()  //realm 데이터 받기.

        var iv = findViewById<View>(R.id.bg_view) as ImageView  //xml의 배경이미지 화면을 iv로 받아옴.
        val vto = iv.getViewTreeObserver()

        //뷰가 만들어지고 실행하도록 변경
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val arr = intent.getByteArrayExtra("image")
                val bm = BitmapFactory.decodeByteArray(arr, 0, arr.size)
                val infoCCTV = intent.getParcelableExtra<CCTV>("cctv") // CCTVDetailActivity 에서 보낸 객체 받아옴 (CCTV 장소)

                var canvas = Canvas()
                val resized = Bitmap.createScaledBitmap(bm, iv.width, iv.height, true)  //이미지 크기 조절
                iv.setImageBitmap(resized)


                //취소버튼. 현재-> 누르면 좌표 초기화 후 액티비티 종료.
                cancleButton.setOnClickListener {
                    Companion.vx.clear()
                    Companion.vy.clear()
                    Companion.relative_vx.clear()
                    Companion.relative_vy.clear()

                    realm.close()
                    finish()
                }
                //확인 버튼.
                confirmButton.setOnClickListener {
                    convert_pointlist()     //모든 좌표를 저장하여 coordinate에 담아주는 함수.
                    realm.beginTransaction()    // 온전한 데이터 저장을 위한 트랙잭션 On
                    // 현재 장소의 데이터를 가져옴
                    val updateItem = realm.where<CCTVRealmStruct>(CCTVRealmStruct::class.java).equalTo("Location", infoCCTV.name)?.findFirst()

                    // 해당장소에 대한 데이터가 없으면 좌표 추가(최초 1번만 시행) , Json 메시지 생성
                    if (updateItem == null) {
                        //고유 키id 설정(1부터 1씩 증가)
                        val currentId = realm.where<CCTVRealmStruct>(CCTVRealmStruct::class.java).max("id")
                        val nextId = if (currentId == null) 1 else currentId.toInt() + 1
                        val newObject = realm.createObject<CCTVRealmStruct>(nextId)

                        //realm에 상대좌표 등록
                        if (stateButton1.isChecked) newObject.Intrusion = relative_coordinates.toString()
                        else newObject.Intrusion = "[]"

                        if (stateButton2.isChecked) newObject.Loitering = relative_coordinates.toString()
                        else newObject.Loitering = "[]"

                        if (stateButton3.isChecked) newObject.Abandon = relative_coordinates.toString()
                        else newObject.Abandon = "[]"

                        if (stateButton4.isChecked) newObject.Falldown = relative_coordinates_full.toString()
                        else newObject.Falldown = "[]"

                        //Json 메시지 생성
                        sObject.put("Intrusion", newObject.Intrusion)
                        sObject.put("Loitering", newObject.Loitering)
                        sObject.put("Abandon", newObject.Abandon)
                        sObject.put("Falldown", newObject.Falldown)

                        //realm에 장소 등록
                        newObject.Location = infoCCTV?.name


                    }
                    //해당 장소가 realm에 있을 경우 해당 장소에 대한 좌표 업데이트, Json 메시지 생성.
                    else if (infoCCTV?.name == updateItem.Location.toString()) {
                        updateItem.Location = infoCCTV.name

                        if (stateButton1.isChecked) {
                            updateItem.Intrusion = relative_coordinates.toString()
                        } else {
                            updateItem.Intrusion = "[]"
                            sObject.remove("Intrusion")
                        }
                        sObject.put("Intrusion", updateItem?.Intrusion)

                        if (stateButton2.isChecked) {
                            updateItem.Loitering = relative_coordinates.toString()
                        }
                        else {
                            updateItem.Loitering = "[]"
                            sObject.remove("Loitering")
                        }
                        sObject.put("Loitering", updateItem?.Loitering)

                        if (stateButton3.isChecked) {
                            updateItem.Abandon = relative_coordinates.toString()
                        }
                        else {
                            updateItem.Abandon = "[]"
                            sObject.remove("Abandon")
                        }
                        sObject.put("Abandon", updateItem?.Abandon)

                        if (stateButton4.isChecked) {
                            updateItem.Falldown = relative_coordinates_full.toString()
                        }
                        else {
                            updateItem.Falldown = "[]"
                            sObject.remove("Falldown")
                        }
                        sObject.put("Falldown", updateItem?.Falldown)

                    }

                    val viewItem = realm.where<CCTVRealmStruct>(CCTVRealmStruct::class.java).equalTo("Location", infoCCTV.name)?.findFirst() //쿼리문
                    Log.d(sObject.toString(), "- JsonMessage -")

                    realm.commitTransaction()       //위에서 변경한 데이터 커밋

                    //좌표 초기화
                    Companion.vx.clear()
                    Companion.vy.clear()
                    Companion.relative_vx.clear()
                    Companion.relative_vy.clear()

                    postArea()

                    //액티비티 종료
                    realm.close()
                    finish()
                }

                mPaint.setColor(Color.BLUE)
                mPaint.setStrokeWidth(10F)
                mPaint.setStyle(Paint.Style.STROKE)
                mPaint.setAntiAlias(true)
                mPaint.setDither(true)

                canvas.setBitmap(resized)
                iv.setOnTouchListener() { view, motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            val points = ArrayList<Float>()// 임시 좌표비
                            x = motionEvent.getX()      //현재좌표를 받아옴.
                            y = motionEvent.getY()
                            val relative_coordinate_X = x / iv.width        //현재좌표를 상대좌표로 저장.
                            val relative_coordinate_Y = y / iv.height

                            //좌표벡터에 넣어준다.
                            if (input_flag == 1) {
                                vx.add(x)
                                vy.add(y)
                                CCTVSetDomainActivity.vx.add(x)
                                CCTVSetDomainActivity.vy.add(y)
                                CCTVSetDomainActivity.relative_vx.add(x / iv.width)
                                CCTVSetDomainActivity.relative_vy.add(y / iv.height)

                            }
                        }
                    }
                    onDraw(canvas)
                    iv.invalidate()
                    return@setOnTouchListener true

                }
            }
        })

    }

    fun postArea() {
        //네트워크 Examples
        getCCTVNetworkInstance()
                .postDetectionArea(PostDetectionAreaRequest(sObject.getString("Intrusion"), sObject.getString("Loitering"), sObject.getString("Abandon"), sObject.getString("Falldown")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    Log.d("tagg result ", it.toString())
                },
                        {
                            error->
                        })
    }

    //좌표들을 coordanates에 저장하는 함수
    fun convert_pointlist() {
        if (coordinates.size != 0)
            return

        for (i in 0 until Companion.vx.size - 1) {
            // points : x,y값을 저장하는 ArrayList
            val points = java.util.ArrayList<Float>()
            val relative_points = java.util.ArrayList<Float>()
            points.add(Companion.vx[i]) //저장된 기본 좌표를 추가.
            points.add(Companion.vy[i])
            relative_points.add(Math.round(relative_vx[i] * 100) / 100.0f)  //저장된 상대좌표를 소숫점 2자리로 변경후 추가.
            relative_points.add(Math.round(relative_vy[i] * 100) / 100.0f)

            coordinates.add(points)     //모든 좌표 추가
            relative_coordinates.add(relative_points)       //모든 상대좌표 추가
        }

        //배경이미지의 최대 좌표값을 저장하는 부분
        var iv2 = findViewById<View>(R.id.bg_view) as ImageView
        for (i in 0..3) {
            val points = java.util.ArrayList<Int>()
            val relative_points = java.util.ArrayList<Int>()

            if (i == 0) {
                points.add(0)
                points.add(0)
                relative_points.add(0)
                relative_points.add(0)
            } else if (i == 1) {
                points.add(iv2.getWidth())
                points.add(0)
                relative_points.add(1)
                relative_points.add(0)
            } else if (i == 2) {
                points.add(iv2.getWidth())
                points.add(iv2.getHeight())
                relative_points.add(1)
                relative_points.add(1)
            } else {
                points.add(0)
                points.add(iv2.getHeight())
                relative_points.add(0)
                relative_points.add(1)
            }

            coordinates_full.add(points)
            relative_coordinates_full.add(relative_points)
        }
    }

}


