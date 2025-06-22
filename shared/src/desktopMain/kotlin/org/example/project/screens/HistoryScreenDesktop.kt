package org.example.project.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.project.screens.HistoryViewModel
import org.example.project.screens.HistoryEvent
import org.example.project.data.model.ChatSession
import org.koin.compose.koinInject
import org.example.project.localization.LocalLocalizationManager

@Composable
fun HistoryScreenDesktop(
    viewModel: HistoryViewModel = koinInject(),
    onSessionSelected: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val localizationManager = LocalLocalizationManager.current
    
    println("HistoryScreen: sessions count: ${state.sessions.size}")
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HistoryEffect.NavigateToChat -> {
                    onSessionSelected(effect.sessionId)
                }
                is HistoryEffect.ShowError -> {
                    // TODO: Show error toast
                }
                is HistoryEffect.ShowSuccess -> {
                    // TODO: Show success toast
                }
                is HistoryEffect.ShowClearConfirmation -> {
                    // TODO: Show confirmation dialog
                }
            }
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localizationManager.getString("history_title"),
                style = MaterialTheme.typography.headlineSmall
            )
            IconButton(
                onClick = { viewModel.handleEvent(HistoryEvent.ClearAllHistory) }
            ) {
                Icon(Icons.Default.Delete, contentDescription = localizationManager.getString("delete"))
            }
        }
        
        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.filterType == FilterType.ALL,
                onClick = { viewModel.handleEvent(HistoryEvent.SetFilter(FilterType.ALL)) },
                label = { Text(localizationManager.getString("history_filter_all")) }
            )
            FilterChip(
                selected = state.filterType == FilterType.TEXT_ONLY,
                onClick = { viewModel.handleEvent(HistoryEvent.SetFilter(FilterType.TEXT_ONLY)) },
                label = { Text(localizationManager.getString("history_filter_text")) }
            )
            FilterChip(
                selected = state.filterType == FilterType.WITH_IMAGE,
                onClick = { viewModel.handleEvent(HistoryEvent.SetFilter(FilterType.WITH_IMAGE)) },
                label = { Text(localizationManager.getString("history_filter_image")) }
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
                        text = localizationManager.getString("history_empty"),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = localizationManager.getString("chat_new_session"),
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
                    DesktopSessionCard(
                        session = session,
                        onSessionClick = { viewModel.handleEvent(HistoryEvent.OpenSession(session.id)) },
                        onDeleteClick = { viewModel.handleEvent(HistoryEvent.DeleteSession(session.id)) }
                    )
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

@Composable
fun DesktopSessionCard(
    session: org.example.project.data.model.ChatSession,
    onSessionClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val localizationManager = LocalLocalizationManager.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onSessionClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Icon(
                imageVector = if (session.hasImage) Icons.Default.Image else Icons.Default.Chat,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = session.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = session.timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Delete button
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = localizationManager.getString("history_delete"),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
} 