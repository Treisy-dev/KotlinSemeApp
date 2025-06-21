package org.example.project.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.project.navigation.SharedScreen

@Composable
fun ChatScreen() {
    val navigator = LocalNavigator.currentOrThrow

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { navigator.push(SharedScreen.Settings) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }

        Column(
            modifier = Modifier.weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Тут будет история чата")
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

object ChatScreenRoute : Screen {
    @Composable
    override fun Content() {
        ChatScreen()
    }
} 