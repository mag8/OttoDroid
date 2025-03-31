package com.example.ottov1.ui.activitylist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ottov1.R
import com.example.ottov1.data.model.ClimbingActivity
import com.example.ottov1.ui.theme.OttoListBackground
import com.example.ottov1.ui.theme.OttoListBorder
import com.example.ottov1.ui.theme.OttoListSubtleText
import com.example.ottov1.ui.theme.OttoListTitle
import java.text.SimpleDateFormat
import java.util.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityListScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToActivity: (Long) -> Unit,
    viewModel: ActivityListViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val activities by viewModel.activities.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.activity_horizontal_margin))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.activity_list_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = OttoListTitle
                    )
                    Row {
                        IconButton(onClick = onNavigateToMap) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_map_24),
                                contentDescription = stringResource(R.string.activity_list_map_view_description),
                                tint = OttoListTitle
                            )
                        }
                        IconButton(onClick = { /* TODO: Filter */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter_alt_24),
                                contentDescription = stringResource(R.string.activity_list_filter_description),
                                tint = OttoListTitle
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.activity_list_search_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = stringResource(R.string.activity_list_search_description),
                            tint = OttoListTitle
                        )
                    },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.spacing_large)),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OttoListBorder,
                        focusedBorderColor = OttoListTitle
                    ),
                    singleLine = true
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = OttoListBackground
        ) {
            if (activities.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(R.dimen.spacing_extra_large)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.activity_list_no_activities),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OttoListSubtleText
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = dimensionResource(R.dimen.activity_horizontal_margin),
                        vertical = dimensionResource(R.dimen.spacing_medium)
                    ),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                ) {
                    items(activities) { activity ->
                        ActivityItem(
                            activity = activity,
                            onClick = { onNavigateToActivity(activity.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityItem(
    activity: ClimbingActivity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        colors = CardDefaults.cardColors(
            containerColor = OttoListBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.spacing_extra_small)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.spacing_large))
                .fillMaxWidth()
        ) {
            Text(
                text = activity.name,
                style = MaterialTheme.typography.titleMedium,
                color = OttoListTitle
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDateForList(activity.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = OttoListSubtleText
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.activity_list_view_details_description),
                    tint = OttoListSubtleText
                )
            }
            if (!activity.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OttoListSubtleText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun formatDateForList(timestamp: Long): String {
    return try {
        val instant = Instant.ofEpochMilli(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(localDateTime)
    } catch (e: Exception) {
        SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(timestamp))
    }
} 