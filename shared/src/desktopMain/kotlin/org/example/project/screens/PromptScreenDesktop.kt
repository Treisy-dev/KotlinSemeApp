package org.example.project.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.screens.PromptViewModel
import org.example.project.screens.PromptEvent
import org.koin.compose.koinInject
import org.example.project.platform.LocalImage

@Composable
fun PromptScreenDesktop(
    viewModel: PromptViewModel = koinInject(),
    onNavigateToChat: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    
    println("PromptScreen: prompt changed to: '${state.prompt}'")
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PromptEffect.NavigateToChat -> {
                    onNavigateToChat(effect.sessionId)
                }
                is PromptEffect.ShowError -> {
                    println("Error: ${effect.message}")
                }
                is PromptEffect.ShowSuccess -> {
                    println("Success: ${effect.message}")
                }
            }
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New Chat",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = state.prompt,
                    onValueChange = { 
                        println("PromptScreen TextField onValueChange: $it")
                        viewModel.handleEvent(PromptEvent.UpdatePrompt(it)) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    placeholder = { Text("Enter your prompt...") },
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (state.prompt.isNotBlank()) {
                                viewModel.handleEvent(PromptEvent.SendPrompt)
                            }
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        IconButton(
                            onClick = { 
                                println("Desktop: PickImage button clicked")
                                viewModel.handleEvent(PromptEvent.PickImage) 
                            }
                        ) {
                            Icon(Icons.Default.Image, contentDescription = "Add image")
                        }
                        if (state.imagePath != null) {
                            IconButton(
                                onClick = { 
                                    println("Desktop: ClearImage button clicked")
                                    viewModel.handleEvent(PromptEvent.ClearImage) 
                                }
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear image")
                            }
                        }
                    }
                    
                    Button(
                        onClick = { 
                            println("Desktop: SendPrompt button clicked")
                            viewModel.handleEvent(PromptEvent.SendPrompt) 
                        },
                        enabled = state.prompt.isNotBlank() && !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Send, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send")
                    }
                }
                
                // Image preview
                state.imagePath?.let { imagePath ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LocalImage(
                                path = imagePath,
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "Selected image"
                            )
                        }
                    }
                }
            }
        }
        
        // Error display
        state.error?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
} 