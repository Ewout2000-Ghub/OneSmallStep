package com.example.onesmallstep.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.entities.Phobia
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.ui.detail.PhobiaDetailActivity
import com.example.onesmallstep.ui.theme.OneSmallStepTheme
import com.example.onesmallstep.viewmodel.HomeViewModel
import com.example.onesmallstep.viewmodel.HomeViewModelFactory

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            PhobiaRepository(
                PhobiaDatabase.getDatabase(application).phobiaDao(),
                PhobiaDatabase.getDatabase(application).progressDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneSmallStepTheme {
                HomeScreen(
                    viewModel = viewModel,
                    onPhobiaClick = { phobia ->
                        val intent = Intent(this@HomeActivity, PhobiaDetailActivity::class.java).apply {
                            putExtra("PHOBIA_ID", phobia.id)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPhobiaClick: (Phobia) -> Unit
) {
    val phobias by viewModel.phobias.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }

    val gradientBlue = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E40AF),
            Color(0xFF3B82F6),
            Color(0xFF60A5FA)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBlue)
    ) {
        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E40AF))
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp)
        ) {
            // Logo and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚀", fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "OneSmallStep",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Overcome your fears",
                        fontSize = 14.sp,
                        color = Color(0xFFBFDBFE)
                    )
                }
            }

            // Search Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF6B7280)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            if (query.isBlank()) {
                                viewModel.loadAllPhobias()
                            } else {
                                viewModel.searchPhobias(query)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search phobias...",
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // Filter Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    text = "All",
                    selected = selectedFilter == "all",
                    onClick = {
                        selectedFilter = "all"
                        viewModel.loadAllPhobias()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    text = "Common",
                    selected = selectedFilter == "common",
                    onClick = {
                        selectedFilter = "common"
                        viewModel.loadPhobiasByCategory("common")
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    text = "Specific",
                    selected = selectedFilter == "rare",
                    onClick = {
                        selectedFilter = "rare"
                        viewModel.loadPhobiasByCategory("rare")
                    }
                )
            }
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(16.dp)
        ) {
            // Stats Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatColumn("25+", "Phobias")
                    StatColumn("4 Levels", "Per Program")
                    StatColumn("Evidence", "Based")
                }
            }

            // Phobias Grid
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF3B82F6))
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(phobias) { phobia ->
                        PhobiaCard(
                            phobia = phobia,
                            onClick = { onPhobiaClick(phobia) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color.White else Color(0xFF3730A3)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            color = if (selected) Color(0xFF1E40AF) else Color.White
        )
    }
}

@Composable
fun StatColumn(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFFDBEAFE)
        )
    }
}

@Composable
fun PhobiaCard(
    phobia: Phobia,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getPhobiaEmoji(phobia.iconResource),
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = phobia.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = phobia.scientificName,
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Badge
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (phobia.category == "common")
                        Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (phobia.category == "common") "Common" else "Specific",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    color = if (phobia.category == "common")
                        Color(0xFF065F46) else Color(0xFF92400E)
                )
            }
        }
    }
}

fun getPhobiaEmoji(iconResource: String): String {
    return when (iconResource) {
        "spider" -> "🕷️"
        "height" -> "🏔️"
        "airplane" -> "✈️"
        "microphone" -> "🎤"
        "snake" -> "🐍"
        "dog" -> "🐕"
        "needle" -> "💉"
        "blood" -> "🩸"
        "stomach" -> "🤢"
        "car" -> "🚗"
        "bacteria" -> "🦠"
        "moon" -> "🌙"
        "storm" -> "⛈️"
        "clown" -> "🤡"
        "doll" -> "🪆"
        "holes" -> "🕳️"
        "mirror" -> "🪞"
        "balloon" -> "🎈"
        "dentist" -> "🦷"
        "throat" -> "🫰"
        "hand" -> "✋"
        "field" -> "🌾"
        "skull" -> "💀"
        "people" -> "👥"
        "box" -> "📦"
        else -> "😰"
    }
}