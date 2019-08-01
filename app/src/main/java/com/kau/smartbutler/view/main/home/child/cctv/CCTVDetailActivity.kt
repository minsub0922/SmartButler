package com.kau.smartbutler.view.main.home.child.cctv

//현 문제점 스레드, 해결방법 두가지 버튼(뒤로가기, 다음으로 갈때 쓰레드를 종료한다.
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.Log

import android.view.ViewTreeObserver

import android.widget.Toast
import com.google.gson.JsonObject
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.CCTV
import com.kau.smartbutler.model.Profile
import io.realm.Realm
import io.realm.CCTVRealmStructRealmProxy
import io.realm.ProfileRealmProxy
import kotlinx.android.synthetic.main.activity_cctv_detail.*
import kotlinx.android.synthetic.main.activity_cctv_detail.moreNavButton
import kotlinx.android.synthetic.main.activity_cctv_set_domain.*
import kotlinx.android.synthetic.main.activity_my_profile_modify.*
//import kotlinx.android.synthetic.main.activity_my_profile_modify.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException

import java.net.ProtocolException
import java.net.SocketTimeoutException
import java.net.URL
import java.util.*
import kotlinx.coroutines.*


class CCTVDetailActivity(

        override val layoutRes: Int = R.layout.activity_cctv_detail,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {
    ////////////////////////////////////////////////////////////////////////////////////////////////////
//use variable
    internal var handler = Handler()
    internal var OffActivity = false

    lateinit var realm: Realm
    internal var conf: Bitmap.Config = Bitmap.Config.ARGB_8888
    internal var giving_image = Bitmap.createBitmap(300, 300, conf)
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    internal var coordinates = ArrayList<ArrayList<Float>>()
    internal var ctx: Context = this
    var canvas = Canvas()
    var mPaint = Paint()

    lateinit var cctv_ip: String
    lateinit var cctv_url: String


    override var isChildActivity: Boolean = true
    lateinit var res: JsonObject
    override fun setupView() {
        super.setupView()
        Realm.init(this)

        val infoCCTV: CCTV

        if (intent.hasExtra("cctv")) {
            infoCCTV = intent.getParcelableExtra<CCTV>("cctv")
        } else {
            infoCCTV = CCTV(1, "현관")
        }

        var detected_info = ""
        if (intent.hasExtra("Detected_Event") && !intent.getStringExtra("Detected_Event").equals("")) {
            detected_info = intent.getStringExtra("Detected_Event")
        }
        detectedInfoTextView.setText(detected_info)

        realm = Realm.getDefaultInstance()
        var viewItem = realm.where<CCTVRealmStruct>(CCTVRealmStruct::class.java).equalTo("Location", infoCCTV.name)?.findFirst()
        var profileItem = realm.where<Profile>(Profile::class.java).findFirst()
        if (profileItem != null) {
            cctv_ip = (profileItem as ProfileRealmProxy).`realmGet$cctvIP`().toString()
            cctv_url = "http://$cctv_ip:25001/cgi-bin/viewer/video.jpg?resolution=640x480"
        } else {
            finish()
            Toast.makeText(applicationContext, "CCTV IP주소를 입력해주세요.", Toast.LENGTH_LONG).show()
            return
        }

        var is_area = false
        if (viewItem != null) {
            is_area = getArea(viewItem)
        }
//        Toast.makeText(this,viewItem.toString(), Toast.LENGTH_LONG).show()
        realm.close()
        video_view.setLayerType(video_view.layerType, null)

        val vto = video_view.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                video_view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                Log.d("TEST", "StartPoint! ")

                mPaint.setColor(Color.GREEN)
                mPaint.setStrokeWidth(5F)
                mPaint.setStyle(Paint.Style.STROKE)
                mPaint.setAntiAlias(true)
                mPaint.setDither(true)

                var first_loop = true       //최초 연결시에만 사용하기 위함
                val url = URL(cctv_url)
                val job = Job()

                    CoroutineScope(Dispatchers.Default + job).launch {
                        // = thread run. // Dispatchers.XX는 코루틴의 스레드를 어떠한 형태로 가져갈지를 지정해줌.
                        while (true) {
                            try {
                                if (isFinishing) {
                                    break
                                }
                                Log.d("TEST", "running! ")
                                val t_connection = url.openConnection()
                                if (first_loop) {
                                    t_connection.connectTimeout = 3000 //클라이언트와 서버가 연결이 안되었을 경우 ->SocketTimeoutException
                                    t_connection.readTimeout = 4000 //연결이 되었지만 I/O작업이 길어지거나 락이 걸려 요청이 처리되지 못하고 있을 경우 -> IOException
                                    first_loop = false
                                }

                                delay(200L)     // = thread.sleep
                                val input_stream = t_connection.getInputStream()
                                val image_readBytes = input_stream.readBytes()
                                val bm = BitmapFactory.decodeByteArray(image_readBytes, 0, image_readBytes.size)
                                val resized = Bitmap.createScaledBitmap(bm, video_view.width, video_view.height, false)
                                val width = 700 // 축소시킬 너비
                                val height = 525 // 축소시킬 높이
                                val resized2 = Bitmap.createScaledBitmap(bm, width, height, false)
                                giving_image = resized2
                                val lan = launch(Dispatchers.Main) {
                                    canvas.setBitmap(resized)
                                    if (is_area) {
                                        onDraw(canvas)
                                    }
                                    video_view.setImageBitmap(resized) //비트맵 객체로 보여주기
                                }

                            } catch (e: ProtocolException) {
                                Log.d("Error: ", e.toString())
                            }
                            ////---   연결이 되지 않았을 경우   ---////
                            catch (e: SocketTimeoutException) {
                                Handler(Looper.getMainLooper()).post {
                                    //Thread 안에서 Toast 접근하는 방법
                                    Toast.makeText(applicationContext, "연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                                }
                                Log.d("SOCKET_ERROR: ", e.toString())
                                delay(300L)
                                finish()
                            }
                            ////---   연결은 되었으나 데이터를 못받아 올 경우   ---////
                            catch (e: IOException) {
                                Handler(Looper.getMainLooper()).post {
                                    //Thread 안에서 Toast 접근하는 방법
                                    Toast.makeText(applicationContext, "통신상태를 확인해주세요.", Toast.LENGTH_SHORT).show()
                                }
                                Log.d("IO_ERROR: ", e.toString())
                                delay(300L)
                                finish()
                            }
                        }
                        job.join()
                    }
            }
        })

    moreNavButton.setOnClickListener{
        ////////////////////////////////////////////////////////////////////////////////////////////////////
//버튼 클릭 리스너

        val bStream = ByteArrayOutputStream()
        giving_image.compress(Bitmap.CompressFormat.PNG, 90, bStream)
        val byteArray = bStream.toByteArray()
        val i = Intent(this, CCTVSetDomainActivity::class.java)
        i.putExtra("image", byteArray)
        i.putExtra("cctv", infoCCTV)
        OffActivity = true

        finish()
        startActivity(i)
////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}

fun getArea(area_info: CCTVRealmStruct?): Boolean {
    var points_list: List<String> = ArrayList<String>()
    var draw_points = ArrayList<Float>()

    if ((area_info as CCTVRealmStructRealmProxy).`realmGet$Abandon`() != "[]") {
        points_list = (area_info as CCTVRealmStructRealmProxy).`realmGet$Abandon`().replace("[^0-9.,]".toRegex(), "").split(',')
    } else if ((area_info as CCTVRealmStructRealmProxy).`realmGet$Loitering`() != "[]") {
        points_list = (area_info as CCTVRealmStructRealmProxy).`realmGet$Loitering`().replace("[^0-9.,]".toRegex(), "").split(',')
    } else if ((area_info as CCTVRealmStructRealmProxy).`realmGet$Intrusion`() != "[]") {
        points_list = (area_info as CCTVRealmStructRealmProxy).`realmGet$Intrusion`().replace("[^0-9.,]".toRegex(), "").split(',')
    }

    if (points_list.isEmpty()) {
        return false
    }
    for (index in 0..points_list.size - 1) {
        draw_points.add(points_list[index].toFloat())

        if (index % 2 != 0) {
            coordinates.add(draw_points)
            draw_points = ArrayList<Float>()
        }
    }
    return true
}

internal fun onDraw(canvas: Canvas) {

    var drawX = (coordinates[0][0] * video_view.width)
    var drawY = (coordinates[0][1] * video_view.height)
    canvas.drawCircle(drawX, drawY, 10f, mPaint)
    for (i in 1 until coordinates.size) {
        var predrawX = coordinates[i - 1][0] * video_view.width
        var predrawY = coordinates[i - 1][1] * video_view.height
        var nowdrawX = coordinates[i][0] * video_view.width
        var nowdrawY = coordinates[i][1] * video_view.height

        canvas.drawCircle(nowdrawX, nowdrawY, 10f, mPaint)
        canvas.drawLine(nowdrawX, nowdrawY, predrawX, predrawY, mPaint)

    }
    canvas.drawLine(drawX, drawY, coordinates[coordinates.size - 1][0] * video_view.width, coordinates[coordinates.size - 1][1] * video_view.height, mPaint) //최종좌표와 시작점 선긋기
}

override fun onBackPressed() {
    finish()
    super.onBackPressed()
}


}