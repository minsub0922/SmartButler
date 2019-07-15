package com.kau.smartbutler.view.main.butler

import com.kau.smartbutler.model.Conversation
import com.kau.smartbutler.model.Device

class ConversationSingleton() {

    val list by lazy {
        ArrayList<Conversation>()
    }

    companion object {
        var INSTANCE: ConversationSingleton? = null

        fun getInstance(): ConversationSingleton {
            if (INSTANCE == null)
                INSTANCE = ConversationSingleton()
            return INSTANCE!!
        }

        fun newInstance(): ConversationSingleton {
            return ConversationSingleton()
        }
    }
}