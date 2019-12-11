package com.kau.smartbutler

import android.app.*
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.kau.smartbutler.util.network.getCCTVNetworkInstance
import com.kau.smartbutler.util.notification.EventDetectionNotification
import com.kau.smartbutler.view.main.home.child.cctv.CCTVDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EventDetectionService : Service() {
    private var isStop: Boolean = false
    private val request_thread = Thread(RequestDetectedEvent())
    private var eventDetection_Notification = EventDetectionNotification()

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
                if (isStop){
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

                            var send_data = ""

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

                            eventDetection_Notification.createNotification(applicationContext ,send_data)

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
}
