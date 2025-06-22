package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.project.screens.*
import org.koin.mp.KoinPlatform.getKoin
import androidx.compose.ui.platform.LocalContext
import java.io.File
import org.example.project.platform.LocalImage

@Composable
fun ChatScreen() {
    val viewModel = getKoin().get<ChatViewModel>()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Messages list
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Start a conversation",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ask Gemini anything or share an image for analysis",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(state.messages) { message ->
                    MessageCard(
                        message = message,
                        onShareClick = { viewModel.handleEvent(ChatEvent.ShareMessage(message)) }
                    )
                }
            }
            
            // Loading indicator
            if (state.isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Gemini is thinking...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Input area
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                TextField(
                    value = state.currentMessage,
                    onValueChange = { viewModel.handleEvent(ChatEvent.UpdateMessage(it)) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message...") },
                    maxLines = 4,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { 
                        if (state.currentMessage.isNotBlank()) {
                            viewModel.handleEvent(ChatEvent.SendMessage(state.currentMessage))
                        }
                    },
                    enabled = state.currentMessage.isNotBlank() && !state.isLoading
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (state.currentMessage.isNotBlank() && !state.isLoading) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
    
    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(state.messages.size - 1)
            }
        }
    }
}

@Composable
fun HistoryScreen() {
    val viewModel = getKoin().get<HistoryViewModel>()
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.filterType == FilterType.ALL,
                onClick = { viewModel.handleEvent(HistoryEvent.SetFilter(FilterType.ALL)) },
                label = { Text("All") }
            )
            FilterChip(
                selected = state.filterType == FilterType.TEXT_ONLY,
                onClick = { viewModel.handleEvent(HistoryEvent.SetFilter(FilterType.TEXT_ONLY)) },
                label = { Text("Text Only") }
            )
            FilterChip(
                selected = state.filterType == FilterType.WITH_IMAGE,
                onClick = { viewModel.handleEvent(HistoryEvent.SetFilter(FilterType.WITH_IMAGE)) },
                label = { Text("With Image") }
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Sessions list
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.sessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No chat history",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start a new conversation to see it here",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.sessions) { session ->
                    SessionCard(
                        session = session,
                        onSessionClick = { viewModel.handleEvent(HistoryEvent.OpenSession(session.id)) },
                        onDeleteClick = { viewModel.handleEvent(HistoryEvent.DeleteSession(session.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun PromptScreen() {
    val viewModel = getKoin().get<PromptViewModel>()
    val state by viewModel.state.collectAsState()
    
    val imagePicker = rememberImagePicker { imagePath ->
        println("Android PromptScreen: ImagePicker callback with path: $imagePath")
        if (imagePath != null) {
            viewModel.handleEvent(PromptEvent.SelectImage(imagePath))
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
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
                            println("Android PromptScreen: Gallery button clicked")
                            imagePicker.pickFromGallery() 
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gallery")
                    }
                    
                    Button(
                        onClick = { 
                            println("Android PromptScreen: Camera button clicked")
                            imagePicker.takePhoto() 
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
                                    println("Android PromptScreen: Clear image button clicked")
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
                    onValueChange = { viewModel.handleEvent(PromptEvent.UpdatePrompt(it)) },
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
            onClick = { viewModel.handleEvent(PromptEvent.SendPrompt) },
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

@Composable
fun SettingsScreen() {
    val viewModel = getKoin().get<SettingsViewModel>()
    val state by viewModel.state.collectAsState()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Appearance section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Appearance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Dark mode toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Dark Mode",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Use dark theme",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = state.isDarkMode,
                            onCheckedChange = { viewModel.handleEvent(SettingsEvent.SetDarkMode(it)) }
                        )
                    }
                }
            }
        }
        
        // Language section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Language",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = state.language == "en",
                            onClick = { viewModel.handleEvent(SettingsEvent.SetLanguage("en")) },
                            label = { Text("English") }
                        )
                        FilterChip(
                            selected = state.language == "ru",
                            onClick = { viewModel.handleEvent(SettingsEvent.SetLanguage("ru")) },
                            label = { Text("Русский") }
                        )
                    }
                }
            }
        }
        
        // Data management section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Data Management",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { viewModel.handleEvent(SettingsEvent.ClearAllData) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Clear All Data")
                    }
                }
            }
        }
    }
} 