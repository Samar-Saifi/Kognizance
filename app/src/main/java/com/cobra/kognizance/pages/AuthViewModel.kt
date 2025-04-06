package com.cobra.kognizance.pages

import android.os.Message
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cobra.kodes.Constants
import com.cobra.kodes.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private  val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init{
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String){

        if(email.trim().isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty!!")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email.trim(),password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun SignUp(email: String, password: String){

        if(email.trim().isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty!!")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email.trim(),password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun SignOut(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }



    ///Chat ViewModel

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

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String) : AuthState()
}