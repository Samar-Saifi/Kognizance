package com.cobra.kodes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class Chat_VM: ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro-latest",
        apiKey = Constants.API_KEY
    )

    fun SendMessage(question: String){
        viewModelScope.launch {

            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role){text(it.message).toString()}
                    }.toList()
                )
                messageList.add(MessageModel(question , "user"))
                val response = chat.sendMessage(question)
                messageList.add(MessageModel(response.text.toString() , "model"))
            }catch(e: Exception){
                messageList.add((MessageModel("Error : " + e.message.toString(), "model")))
            }
        }
    }
}