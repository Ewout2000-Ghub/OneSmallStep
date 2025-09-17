package com.example.onesmallstep.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onesmallstep.MainActivity
import com.example.onesmallstep.ui.theme.OneSmallStepTheme

class IntroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneSmallStepTheme {
                IntroScreen(
                    onGetStarted = {
                        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroScreen(
    onGetStarted: () -> Unit = {}
) {
    val gradientBlue = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E3A8A), // Dark blue
            Color(0xFF3B82F6), // Medium blue
            Color(0xFF60A5FA)  // Light blue
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Top spacer
            Spacer(modifier = Modifier.height(60.dp))

            // Logo and main content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo placeholder - you can replace with your actual logo
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🚀",
                        fontSize = 48.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "OneSmallStep",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Overcome your fears, one step at a time",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Features section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Evidence-Based Exposure Therapy",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E3A8A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    FeatureItem(
                        emoji = "📚",
                        title = "25+ Common Phobias",
                        description = "From spiders to public speaking"
                    )

                    FeatureItem(
                        emoji = "📈",
                        title = "Step-by-Step Plans",
                        description = "Gradual exposure at your pace"
                    )

                    FeatureItem(
                        emoji = "🏆",
                        title = "Track Progress",
                        description = "Monitor your journey to confidence"
                    )

                    FeatureItem(
                        emoji = "🔒",
                        title = "Safe Environment",
                        description = "Professional therapeutic approach"
                    )
                }
            }

            // Call to action
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1E3A8A)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Start Your Journey",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Take the first step towards overcoming your fears today",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun FeatureItem(
    emoji: String,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp,
            modifier = Modifier.padding(end = 16.dp)
        )

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937)
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
            )
        }
    }
}