package com.example.onesmallstep.ui.exposure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.entities.ExposureStep
import com.example.onesmallstep.data.entities.UserProgress
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.ui.theme.OneSmallStepTheme
import com.example.onesmallstep.viewmodel.ExposureViewModel
import com.example.onesmallstep.viewmodel.ExposureViewModelFactory
import java.util.*

class ExposureActivity : ComponentActivity() {

    private val viewModel: ExposureViewModel by viewModels {
        ExposureViewModelFactory(
            PhobiaRepository(
                PhobiaDatabase.getDatabase(application).phobiaDao(),
                PhobiaDatabase.getDatabase(application).progressDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val levelId = intent.getIntExtra("LEVEL_ID", -1)
        val phobiaId = intent.getIntExtra("PHOBIA_ID", -1)

        if (levelId == -1 || phobiaId == -1) {
            finish()
            return
        }

        setContent {
            OneSmallStepTheme {
                ExposureScreen(
                    viewModel = viewModel,
                    levelId = levelId,
                    phobiaId = phobiaId,
                    onBackClick = { finish() },
                    onStepComplete = { step, anxietyRating, notes ->
                        val progress = UserProgress(
                            phobiaId = phobiaId,
                            levelId = levelId,
                            stepId = step.id,
                            isCompleted = true,
                            anxietyRating = anxietyRating,
                            completedDate = Date(),
                            notes = notes
                        )
                        viewModel.saveProgress(progress)
                    }
                )
            }
        }

        viewModel.loadStepsForLevel(levelId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposureScreen(
    viewModel: ExposureViewModel,
    levelId: Int,
    phobiaId: Int,
    onBackClick: () -> Unit,
    onStepComplete: (ExposureStep, Int, String?) -> Unit
) {
    val steps by viewModel.steps.observeAsState(emptyList())
    var showCompletionDialog by remember { mutableStateOf<ExposureStep?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E40AF))
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

                Column {
                    Text(
                        text = "Exposure Steps",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Complete each step at your own pace",
                        fontSize = 16.sp,
                        color = Color(0xFFBFDBFE)
                    )
                }
            }
        }

        // Steps List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(steps) { step ->
                StepCard(
                    step = step,
                    onCompleteClick = { showCompletionDialog = step }
                )
            }
        }
    }

    // Completion Dialog
    showCompletionDialog?.let { step ->
        StepCompletionDialog(
            step = step,
            onDismiss = { showCompletionDialog = null },
            onComplete = { anxietyRating, notes ->
                onStepComplete(step, anxietyRating, notes)
                showCompletionDialog = null
            }
        )
    }
}

@Composable
fun StepCard(
    step: ExposureStep,
    onCompleteClick: () -> Unit
) {
    var isCompleted by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color(0xFFF0FDF4) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with checkbox and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isCompleted = it },
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = "Step ${step.stepNumber}: ${step.title}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = step.description,
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = step.instructions,
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "${step.duration} - ${step.frequency}",
                fontSize = 12.sp,
                color = Color(0xFF9CA3AF),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = {
                    onCompleteClick()
                    isCompleted = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                ),
                enabled = !isCompleted
            ) {
                Text(
                    text = if (isCompleted) "Completed ✓" else "Complete Step",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StepCompletionDialog(
    step: ExposureStep,
    onDismiss: () -> Unit,
    onComplete: (Int, String?) -> Unit
) {
    var anxietyLevel by remember { mutableStateOf(5) }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Complete Step",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "How did this step go?",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Rate your anxiety level during this step:",
                    fontSize = 16.sp,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Anxiety Level: $anxietyLevel/10",
                    fontSize = 14.sp,
                    color = Color(0xFF3B82F6),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Slider(
                    value = anxietyLevel.toFloat(),
                    onValueChange = { anxietyLevel = it.toInt() },
                    valueRange = 0f..10f,
                    steps = 9,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Optional notes:",
                    fontSize = 16.sp,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = {
                        Text("How did this step go? Any observations?")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    minLines = 3,
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onComplete(anxietyLevel, notes.takeIf { it.isNotBlank() })
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        )
                    ) {
                        Text("Complete", color = Color.White)
                    }
                }
            }
        }
    }
}