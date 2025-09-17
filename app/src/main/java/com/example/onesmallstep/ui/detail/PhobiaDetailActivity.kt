package com.example.onesmallstep.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.entities.ExposureLevel
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.ui.exposure.ExposureActivity
import com.example.onesmallstep.ui.home.getPhobiaEmoji
import com.example.onesmallstep.ui.theme.OneSmallStepTheme
import com.example.onesmallstep.viewmodel.PhobiaDetailViewModel
import com.example.onesmallstep.viewmodel.PhobiaDetailViewModelFactory

class PhobiaDetailActivity : ComponentActivity() {

    private val viewModel: PhobiaDetailViewModel by viewModels {
        PhobiaDetailViewModelFactory(
            PhobiaRepository(
                PhobiaDatabase.getDatabase(application).phobiaDao(),
                PhobiaDatabase.getDatabase(application).progressDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val phobiaId = intent.getIntExtra("PHOBIA_ID", -1)
        if (phobiaId == -1) {
            finish()
            return
        }

        setContent {
            OneSmallStepTheme {
                PhobiaDetailScreen(
                    viewModel = viewModel,
                    phobiaId = phobiaId,
                    onBackClick = { finish() },
                    onLevelClick = { level ->
                        val intent = Intent(this@PhobiaDetailActivity, ExposureActivity::class.java).apply {
                            putExtra("LEVEL_ID", level.id)
                            putExtra("PHOBIA_ID", level.phobiaId)
                        }
                        startActivity(intent)
                    }
                )
            }
        }

        viewModel.loadPhobiaDetails(phobiaId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhobiaDetailScreen(
    viewModel: PhobiaDetailViewModel,
    phobiaId: Int,
    onBackClick: () -> Unit,
    onLevelClick: (ExposureLevel) -> Unit
) {
    val phobia by viewModel.phobia.observeAsState()
    val levels by viewModel.levels.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E40AF))
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 24.dp)
        ) {
            // Back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF3730A3))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Phobia Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Phobia header info
            phobia?.let { phobiaData ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getPhobiaEmoji(phobiaData.iconResource),
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = phobiaData.scientificName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Fear of ${phobiaData.name}",
                            fontSize = 16.sp,
                            color = Color(0xFFBFDBFE)
                        )
                    }
                }
            }
        }

        // Content Section
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3B82F6))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    phobia?.let { phobiaData ->
                        // Description Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "About This Phobia",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2937),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                Text(
                                    text = phobiaData.description,
                                    fontSize = 16.sp,
                                    color = Color(0xFF4B5563),
                                    lineHeight = 24.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                // Stats
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF3F4F6))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    StatItem(
                                        value = if (phobiaData.category == "common") "3-6%" else "1-3%",
                                        label = "Population"
                                    )
                                    StatItem(
                                        value = getSuccessRate(phobiaData.name),
                                        label = "Treatment Success"
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    // Reassurance Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💙",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(end = 16.dp)
                            )

                            Column {
                                Text(
                                    text = "You're Not Alone",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                phobia?.let { phobiaData ->
                                    Text(
                                        text = buildReassuranceMessage(phobiaData.name, phobiaData.category),
                                        fontSize = 14.sp,
                                        color = Color(0xFF1D4ED8),
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Treatment Program",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                }

                item {
                    Text(
                        text = "Our evidence-based program consists of 4 carefully designed levels.",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 20.sp
                    )
                }

                // Levels
                items(levels) { level ->
                    LevelCard(
                        level = level,
                        onClick = { onLevelClick(level) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B82F6)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Composable
fun LevelCard(
    level: ExposureLevel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Level number circle
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF3B82F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.levelNumber.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Level ${level.levelNumber}: ${level.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = level.description,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Duration: ${level.estimatedDuration}",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Start →",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3B82F6)
                        )
                    }
                }
            }
        }
    }
}

// Get realistic success rates based on clinical research
fun getSuccessRate(phobiaName: String): String {
    return when (phobiaName.lowercase()) {
        "spiders", "heights", "flying", "snakes", "needles", "blood" -> "80-90%"
        "public speaking", "social situations" -> "70-85%"
        "dogs", "driving", "storms" -> "75-85%"
        "vomiting", "germs", "confined spaces" -> "65-80%"
        "darkness", "clowns", "dolls" -> "70-80%"
        "choking", "being touched" -> "60-75%"
        "death", "open spaces" -> "60-70%"
        else -> "70-80%"
    }
}

fun buildReassuranceMessage(phobiaName: String, category: String): String {
    val commonMessage = when (category) {
        "common" -> "This is one of the most common phobias, affecting millions of people worldwide."
        else -> "Though less common, many people successfully overcome this fear."
    }

    return "You're taking a brave step by addressing your fear of ${phobiaName.lowercase()}. " +
            "$commonMessage Our evidence-based program will guide you through gradual exposure " +
            "at your own pace, helping you build confidence step by step."
}