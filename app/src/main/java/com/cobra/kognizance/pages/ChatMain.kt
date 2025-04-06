package com.cobra.kodes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cobra.kognizance.pages.AuthViewModel

@Composable
fun CreateChatPage(modifier: Modifier = Modifier, viewModel: AuthViewModel){
    Column {
        CreateHeader()
        CreateMessageList(modifier = Modifier.weight(1f), messageList = viewModel.messageList)
        CreateMessageInput(messageSend = {
            viewModel.SendMessage(it)
        })
    }
}

@Composable
fun CreateHeader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Blue)) {
        Text(
            modifier = Modifier.padding(16.dp, 30.dp, 0.dp, 16.dp),
            text = "AppTest" ,
            color = Color.White ,
            fontSize = 24.sp
        )

    }
}

@Composable
fun CreateMessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>){
    LazyColumn(modifier = modifier, reverseLayout = true) {
        items(messageList.reversed()){
            SelectionContainer {
                Text(text = it.message)
            }
        }
    }
}

@Composable
fun CreateMessageInput(messageSend: (String) -> Unit){

    var message by remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
            },
            Modifier.weight(1f)
        )
        IconButton(onClick = {
            if(message.isNotEmpty())messageSend(message)
            message = ""
        }) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
            )
        }
    }
}
