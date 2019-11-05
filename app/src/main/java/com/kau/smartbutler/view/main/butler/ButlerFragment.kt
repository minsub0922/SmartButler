package com.kau.smartbutler.view.main.butler

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.ERROR
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_butler.*
import java.util.*
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.recyclerview.widget.LinearLayoutManager
import com.kau.smartbutler.controller.ConversationAdapter
import com.kau.smartbutler.model.Conversation
import com.kau.smartbutler.util.network.getListNetworkInstance
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import androidx.core.os.HandlerCompat.postDelayed
import com.kau.smartbutler.model.Profile
import com.kau.smartbutler.util.network.getUtasNetworkInstance
import io.realm.Realm
import io.realm.kotlin.where


class ButlerFragment : BaseFragment(), View.OnClickListener {

    override val layoutRes: Int = R.layout.fragment_butler

    lateinit var tts:TextToSpeech
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var ip:String

    val conversationList = ConversationSingleton.getInstance().list
    val conversationAdapter by lazy { ConversationAdapter(activity!!, conversationList) }
    private var realm = Realm.getDefaultInstance()


    companion object {
        var INSTANCE: ButlerFragment? = null

        fun getInstance(): ButlerFragment {
            if (INSTANCE == null)
                INSTANCE = ButlerFragment()
            return INSTANCE!!
        }

        fun newInstance(): ButlerFragment {
            return ButlerFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        conversationAdapter.notifyDataSetChanged()
    }

    override fun setupView(view: View) {
        super.setupView(view)

        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            @Override
            fun onInit(status: Int) {
                if (status != ERROR) {
                    tts.language = Locale.KOREA
                    tts.setSpeechRate(0.7.toFloat())
                }
            }
        })

        realm.beginTransaction()
        val profile = realm.where<Profile>().findFirst()

        realm.commitTransaction()

        if (profile == null) {
            ip = "http://112.169.29.116:8080"
        } else {
            ip = profile.openhapIP
        }
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context!!.packageName)
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        iv_butler_toggle.setOnClickListener(this)

        butlerMic.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer.setRecognitionListener(object: RecognitionListener{
                override fun onReadyForSpeech(params: Bundle?) {
                    Toast.makeText(context,"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
                    clickVoice.text = "말씀해주세요."
                }
                // 음성 인식 준비 완료
                override fun onRmsChanged(rmsdB: Float) {}
                // 음성의 RMS가 바뀌었을 때
                override fun onBufferReceived(buffer: ByteArray?) {}
                // 음성 데이터의 buffer를 받을 수 있다.
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                // 사용자가 말하기 시작할 때
                override fun onEndOfSpeech() {}
                // 사용자의 말이 끝났을 때
                override fun onError(error: Int) {
                    var message = ""
                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO-> message = "오디오 에러";
                        SpeechRecognizer.ERROR_CLIENT -> message = "클라이언트 에러";
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "퍼미션 없음";
                        SpeechRecognizer.ERROR_NETWORK ->  message = "네트워크 에러";
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "네트웍 타임아웃";
                        SpeechRecognizer.ERROR_NO_MATCH -> message = "찾을 수 없음";
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RECOGNIZER가 바쁨";
                        SpeechRecognizer.ERROR_SERVER -> message = "서버 에러";
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "말하는 시간초과";
                        else -> message = "알 수 없는 오류임";
                    }
                    Toast.makeText(context, "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
                }
                // 오류가 발생했을 때
                override fun onResults(results: Bundle) {
                    val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val scores = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)

                    for (index in matches!!.indices) {
                        if (scores[index] > 0.5)
                        {
                            Toast.makeText(context, matches[index]+" : "+scores[index], Toast.LENGTH_LONG).show()
                            getUtasNetworkInstance()
                                    .getUserServlet("dialog", matches[index], ip)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        tts.speak(it.toString(), TextToSpeech.QUEUE_ADD, null, null)
                                        Log.d("tag result ", it.toString())
                                    }, {})
                        }
                    }
                    clickVoice.text = "음성으로 하시려면 터치해주세요."
                }
                // 결과 값을 받음
            })

            speechRecognizer.startListening(sttIntent)
        }

        val manager = LinearLayoutManager(activity)
        butlerMicView.layoutManager = manager
        butlerMicView.setHasFixedSize(true)
        butlerMicView.adapter = conversationAdapter

        iv_butler_send.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_butler_toggle -> {
                layout_butler_mic.visibility = View.GONE
                iv_butler_toggle.visibility = View.GONE
                butlerMicView.visibility = View.VISIBLE
                iv_butler_send.visibility = View.VISIBLE

                initButlerConversation()
                butlerMicView.adapter = conversationAdapter
                Log.d("tag result ", conversationList.size.toString())
            }
            R.id.iv_butler_send -> {
                userConversation(et_butler_text.text.toString())
                getUtasNetworkInstance()
                        .getUserServlet("dialog", et_butler_text.text.toString(), ip)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            butlerConversation(it.toString())
                            Log.d("tag result ", it.toString())
                        }, {})
                et_butler_text.text.clear()
            }
        }
    }

    fun butlerConversation(s: String) {
        conversationList.add(Conversation(0, s))
        conversationAdapter.notifyDataSetChanged()
        Handler().postDelayed(Runnable { butlerMicView.scrollToPosition(conversationAdapter.getItemCount() - 1) }, 200)
        tts.speak(s, TextToSpeech.QUEUE_ADD, null, null)
    }

    fun userConversation(s: String) {
        conversationList.add(Conversation(1, s))
        conversationAdapter.notifyDataSetChanged()
        Handler().postDelayed(Runnable { butlerMicView.scrollToPosition(conversationAdapter.getItemCount() - 1) }, 200)

    }
    fun initButlerConversation() {
        conversationList.clear()
        butlerConversation("안녕하세요.")
        butlerConversation("제 이름은 버틀러에요.")
        butlerConversation("무엇을 도와드릴까요?")
    }

}