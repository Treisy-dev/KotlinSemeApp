package org.example.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.project.screens.ChatScreenRoute
import org.example.project.screens.SettingsScreenRoute
import org.koin.mp.KoinPlatform.getKoin
import org.example.project.platform.LocalImage

@Composable
fun PromptScreen(viewModel: PromptViewModel, navigator: Navigator) {
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PromptEffect.NavigateToChat -> {
                    navigator.push(ChatScreenRoute())
                }
                is PromptEffect.ShowError -> {
                    // TODO: Show error toast
                }
                is PromptEffect.ShowSuccess -> {
                    // TODO: Show success toast
                }
            }
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigator.pop() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "New Chat",
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(onClick = { navigator.push(SettingsScreenRoute()) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Image selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Add Image (Optional)",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { 
                            println("Common PromptScreen: Gallery button clicked")
                            viewModel.handleEvent(PromptEvent.PickImage) 
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gallery")
                    }
                    
                    Button(
                        onClick = { 
                            println("Common PromptScreen: Camera button clicked")
                            viewModel.handleEvent(PromptEvent.TakePhoto) 
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Camera")
                    }
                }
                
                // Selected image preview
                state.imagePath?.let { imagePath ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            // Load and display image
                            LocalImage(
                                path = imagePath,
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "Selected image"
                            )
                            
                            IconButton(
                                onClick = { 
                                    println("Common PromptScreen: Clear image button clicked")
                                    viewModel.handleEvent(PromptEvent.ClearImage) 
                                },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove image")
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Prompt input
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Your Prompt",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TextField(
                    value = state.prompt,
                    onValueChange = { 
                        println("Common PromptScreen: TextField onValueChange: $it")
                        viewModel.handleEvent(PromptEvent.UpdatePrompt(it)) 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Describe what you want to know or ask...") },
                    minLines = 3,
                    maxLines = 6
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Send button
        Button(
            onClick = { 
                println("Common PromptScreen: Send button clicked")
                viewModel.handleEvent(PromptEvent.SendPrompt) 
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.prompt.isNotBlank() && !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(Icons.Default.Send, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send to Gemini")
        }
        
        // Error display
        state.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

class PromptScreenRoute : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PromptViewModel = getKoin().get()
        PromptScreen(viewModel = viewModel, navigator = navigator)
    }
} 