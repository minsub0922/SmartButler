package com.kau.smartbutler.view.main.home.child.cctv

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import com.google.gson.JsonObject
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.CCTV
import com.kau.smartbutler.model.Profile
import io.realm.CCTVRealmStructRealmProxy
import io.realm.ProfileRealmProxy
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_cctv_detail.*
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*

import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcOptions
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

class CCTVDetailActivity (

        override val layoutRes : Int = R.layout.activity_cctv_detail,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity(), VlcListener{
    override fun onError()
    {
        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show()
    }

    override fun onComplete() {
    }

    internal var handler = Handler()
    internal var OffActivity = false
    lateinit var realm: Realm
    internal var conf: Bitmap.Config = Bitmap.Config.ARGB_8888
    internal var giving_image = Bitmap.createBitmap(300, 300, conf)
    internal var coordinates = ArrayList<ArrayList<Float>>()
    var canvas = Canvas()
    var mPaint = Paint()
    lateinit var cctv_ip : String
    lateinit var cctv_url : String
    override var isChildActivity: Boolean = true
    lateinit var res : JsonObject


    //val surfaceView = findViewById(R.id.video_view) as TextureView
    private val options = arrayOf(":fullscreen")



    override fun setupView() {
        super.setupView()
        Realm.init(this)
        val infoCCTV : CCTV
        if (intent.hasExtra("cctv")){
            infoCCTV = intent.getParcelableExtra<CCTV>("cctv")
        }else{
            infoCCTV = CCTV(1, "현관")
        }
        var detected_info = ""
        if (intent.hasExtra("Detected_Event") && ! intent.getStringExtra("Detected_Event").equals("")){
            detected_info = intent.getStringExtra("Detected_Event")
        }
        detectedInfoTextView.setText(detected_info)
        realm = Realm.getDefaultInstance()
        var viewItem =realm.where<CCTVRealmStruct>(CCTVRealmStruct::class.java).equalTo("Location",infoCCTV.name)?.findFirst()
        var profileItem = realm.where<Profile>(Profile::class.java).findFirst()
        if (profileItem != null){
            cctv_ip = (profileItem as ProfileRealmProxy).`realmGet$cctvIP`().toString()
            cctv_url = "rtsp://admin:123456@$cctv_ip:7070/"
        }
        else{
            finish()
            Toast.makeText(applicationContext,"CCTV  IP 주소를 입력해주세요",Toast.LENGTH_SHORT).show()
            return
        }
        var is_area = false
        if (viewItem != null){
            is_area = getArea(viewItem)
        }
        realm.close()
        var count = 0
        val vlc_videolib = VlcVideoLibrary(this,this, video_view)

        val vto = video_view.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

            override fun onGlobalLayout() {
                mPaint.setColor(Color.GREEN)
                mPaint.setStrokeWidth(5F)
                mPaint.setStyle(Paint.Style.STROKE)
                mPaint.setAntiAlias(true)
                mPaint.setDither(true)
                if (count == 2){

                    vlc_videolib.setOptions(Arrays.asList<String>(*options))
                    vlc_videolib.setWidth_Height(video_view.width, video_view.height)
                    vlc_videolib.play(cctv_url)

                    while(vlc_videolib.player!!.media.stats!!.displayedPictures <= 1){
                    }

                    if (viewItem != null){
                        // 가져온 그림 그리기
                        var bmap = Bitmap.createBitmap(video_view.width, video_view.height, Bitmap.Config.ARGB_8888)
                        canvas.setBitmap(bmap)
                        onDraw(canvas)
                        iv_3.setImageDrawable(BitmapDrawable(resources, bmap))
                    }
                }
                count ++
//                val url = URL(cctv_url)
            }
        })

        moreNavButton.setOnClickListener {
            val bm = video_view.getBitmap()
            val resized = Bitmap.createScaledBitmap(bm, 525, 600, true)
            val bStream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 0, bStream)
            val byteArray = bStream.toByteArray()
            val i = Intent(this, CCTVSetDomainActivity::class.java)
            i.putExtra("image", byteArray)
            i.putExtra("cctv", infoCCTV)
            OffActivity = true
            vlc_videolib.stop()
            finish()
            startActivity(i)
        }
    }
    fun getArea(area_info : CCTVRealmStruct?): Boolean {
        var points_list : List<String> = ArrayList<String>()
        var draw_points = ArrayList<Float>()

        if ( (area_info as CCTVRealmStructRealmProxy).`realmGet$Abandon`() != "[]")
        {
            points_list = (area_info as CCTVRealmStructRealmProxy).`realmGet$Abandon`().replace("[^0-9.,]".toRegex(),"").split(',')
        } else if ((area_info as CCTVRealmStructRealmProxy).`realmGet$Loitering`() != "[]")
        {
            points_list = (area_info as CCTVRealmStructRealmProxy).`realmGet$Loitering`().replace("[^0-9.,]".toRegex(),"").split(',')
        } else if ((area_info as CCTVRealmStructRealmProxy).`realmGet$Intrusion`() != "[]"){
            points_list = (area_info as CCTVRealmStructRealmProxy).`realmGet$Intrusion`().replace("[^0-9.,]".toRegex(),"").split(',')
        }

        if (points_list.isEmpty()){
            return false
        }
        for (index in 0..points_list.size-1){
            draw_points.add(points_list[index].toFloat())

            if (index%2 != 0){
                coordinates.add(draw_points)
                draw_points = ArrayList<Float>()
            }
        }
        return true
    }

    internal fun onDraw(canvas :Canvas) {

        var drawX = (coordinates[0][0] * video_view.width)
        var drawY = (coordinates[0][1] * video_view.height)
        canvas.drawCircle(drawX, drawY, 10f, mPaint)
        for (i in 1 until coordinates.size) {
            var predrawX = coordinates[i-1][0] * video_view.width
            var predrawY = coordinates[i-1][1] * video_view.height
            var nowdrawX = coordinates[i][0] * video_view.width
            var nowdrawY = coordinates[i][1] * video_view.height

            canvas.drawCircle(nowdrawX, nowdrawY, 10f, mPaint)
            canvas.drawLine(nowdrawX, nowdrawY, predrawX, predrawY, mPaint)

        }
        canvas.drawLine(drawX, drawY, coordinates[coordinates.size-1][0]*video_view.width, coordinates[coordinates.size-1][1]*video_view.height, mPaint) //최종좌표와 시작점 선긋기
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

//    override fun on() {
//        super.onResume()

//    }

}

class VlcVideoLibrary : MediaPlayer.EventListener {

    private var width = 0
    private var height = 0
    private var vlcInstance: LibVLC? = null
    var player: MediaPlayer? = null
    private var vlcListener: VlcListener? = null
    //The library will select one of this class for rendering depend of constructor called
    private var surfaceView: SurfaceView? = null
    private var textureView: TextureView? = null
    private var surfaceTexture: SurfaceTexture? = null
    private var surface: Surface? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var options: MutableList<String>? = ArrayList()

    val isPlaying: Boolean
        get() = player != null && player!!.isPlaying

    constructor(context: Context, vlcListener: VlcListener, surfaceView: SurfaceView) {
        this.vlcListener = vlcListener
        this.surfaceView = surfaceView
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    constructor(context: Context, vlcListener: VlcListener, textureView: TextureView) {
        this.vlcListener = vlcListener
        this.textureView = textureView
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    constructor(context: Context, vlcListener: VlcListener, surfaceTexture: SurfaceTexture) {
        this.vlcListener = vlcListener
        this.surfaceTexture = surfaceTexture
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    constructor(context: Context, vlcListener: VlcListener, surface: Surface) {
        this.vlcListener = vlcListener
        this.surface = surface
        surfaceHolder = null
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    constructor(context: Context, vlcListener: VlcListener, surface: Surface,
                surfaceHolder: SurfaceHolder) {
        this.vlcListener = vlcListener
        this.surface = surface
        this.surfaceHolder = surfaceHolder
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    constructor(context: Context, vlcListener: VlcListener, surface: Surface, width: Int,
                height: Int) {
        this.vlcListener = vlcListener
        this.surface = surface
        this.width = width
        this.height = height
        surfaceHolder = null
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    constructor(context: Context, vlcListener: VlcListener, surface: Surface,
                surfaceHolder: SurfaceHolder, width: Int, height: Int) {
        this.vlcListener = vlcListener
        this.surface = surface
        this.surfaceHolder = surfaceHolder
        this.width = width
        this.height = height
        vlcInstance = LibVLC(context, VlcOptions().defaultOptions)
        options!!.add(":fullscreen")
    }

    /**
     * This method should be called after constructor and before play methods.
     *
     * @param options seeted to VLC player.
     */
    fun setOptions(options: MutableList<String>) {
        this.options = options
    }

    fun play(endPoint: String) {
        if (player == null || player!!.isReleased) {
            setMedia(Media(vlcInstance, Uri.parse(endPoint)))
        } else if (!player!!.isPlaying) {
            player!!.play()
        }
    }

    fun stop() {
        if (player != null && player!!.isPlaying) {
            player!!.stop()
            player!!.release()
        }
    }

    fun pause() {
        if (player != null && player!!.isPlaying) {
            player!!.pause()
        }
    }

    fun setWidth_Height(width: Int , height: Int){
        this.width = width
        this.height = height
    }

    private fun setMedia(media: Media) {
        //delay = network buffer + file buffer
        //media.addOption(":network-caching=" + Constants.BUFFER);
        //media.addOption(":file-caching=" + Constants.BUFFER);
        if (options != null) {
            for (s in options!!) {
                media.addOption(s)
            }
        }
        media.setHWDecoderEnabled(true, false)
        player = MediaPlayer(vlcInstance)
        player!!.media = media

        player!!.setEventListener(this)

        val vlcOut = player!!.vlcVout
        //set correct class for render depend of constructor called
        if (surfaceView != null) {
            vlcOut.setVideoView(surfaceView)
            width = surfaceView!!.width
            height = surfaceView!!.height
        } else if (textureView != null) {
            vlcOut.setVideoView(textureView)
            width = textureView!!.width
            height = textureView!!.height
        } else if (surfaceTexture != null) {
            vlcOut.setVideoSurface(surfaceTexture)
        } else if (surface != null) {
            vlcOut.setVideoSurface(surface, surfaceHolder)
        } else {
            throw RuntimeException("You cant set a null render object")
        }
        if (width != 0 && height != 0) vlcOut.setWindowSize(width, height)
        vlcOut.attachViews()
        player!!.setVideoTrackEnabled(true)
        player!!.setAspectRatio(width.toString() + ":"+ height.toString())
        player!!.play()
    }

    override fun onEvent(event: MediaPlayer.Event) {
        when (event.type) {
            MediaPlayer.Event.Playing -> vlcListener!!.onComplete()
            MediaPlayer.Event.EncounteredError -> vlcListener!!.onError()
            else -> {
            }
        }
    }
}