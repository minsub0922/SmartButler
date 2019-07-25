package com.kau.smartbutler

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.kau.smartbutler.util.network.getCCTVNetworkInstance
import com.kau.smartbutler.view.main.home.child.cctv.CCTVDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EventDetectionService : Service() {
    private var isStop: Boolean = false
    private val request_thread = Thread(RequestDetectedEvent())

    override fun onCreate() {
        super.onCreate()
        request_thread.start()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private inner class RequestDetectedEvent : Runnable {
        private val handler = Handler()

        override fun run() {
            while (true) {
                if (isStop or ! isAppOnForeground(applicationContext)) {
                    break
                }

                //이벤트 감지 및 페이지 접근
                getEventInfo()

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.d("Service", "Interrupted")
                }
            }
            handler.post {
                Toast.makeText(applicationContext,
                        "이상상황감지 OFF.",
                        Toast.LENGTH_SHORT).show()
            }
        }

        private fun getEventInfo() {
            var detected_info = ""
            getCCTVNetworkInstance()
                    .getDetectedEvent()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ res ->
                        detected_info = res.getAsJsonPrimitive("Detected_Event").asString

                        if (! detected_info.equals("")){
                            var i = Intent(applicationContext, CCTVDetailActivity::class.java)
                            var send_data = ""
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP)

                            if (detected_info.equals("Intrusion")){
                                send_data = "침입이 감지되었습니다."
                            }
                            else if (detected_info.equals("Loitering")){
                                send_data = "배회가 감지되었습니다."
                            }
                            else if (detected_info.equals("Abandon")){
                                send_data = "유기가 감지되었습니다."
                            }
                            else if (detected_info.equals("Falldown")){
                                send_data = "쓰러짐이 감지되었습니다."
                            }

                            i.putExtra("Detected_Event",send_data)
                            startActivity(i)
                        }
                    },
                            {
                                error ->
                                Log.d("Error",error.toString())
                            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isStop = true
        request_thread.interrupt()
        Log.d("Service", "Stop")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)
        Log.d("Service", "Exit")
        this.stopSelf()
    }

    fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcessInfoList = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcessInfo in appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcessInfo.processName == packageName) {
                return true
            }
        }
        return false
    }
}
