package com.example.onesmallstep

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onesmallstep.ui.home.HomeActivity
import com.example.onesmallstep.ui.intro.IntroActivity
import com.example.onesmallstep.ui.progress.ProgressActivity
import com.example.onesmallstep.ui.theme.OneSmallStepTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val PREFS_NAME = "onesmallstep_prefs"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if this is the first launch
        val prefs: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

        if (isFirstLaunch) {
            // Mark as launched and show intro
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            OneSmallStepTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToHome = {
                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        },
                        onNavigateToProgress = {
                            startActivity(Intent(this@MainActivity, ProgressActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {}
) {
    val gradientBlue = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1E3A8A), // Dark blue
            Color(0xFF3B82F6), // Medium blue
            Color(0xFF60A5FA)  // Light blue
        )
    )

    Box(
        modifier = modifier
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

            Spacer(modifier = Modifier.height(60.dp))

            // Logo and main content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // OneSmallStep Logo - replace with your actual logo
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(70.dp))
                        .background(Color.White.copy(alpha = 0.95f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Astronaut icon from your logo
                        Text(
                            text = "👨‍🚀",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "🌙",
                            fontSize = 24.sp,
                            modifier = Modifier.offset(y = (-8).dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "OneSmallStep",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Overcome your fears, one step at a time",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Quick stats card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("25+", "Phobias")
                    StatItem("4", "Levels")
                    StatItem("Evidence", "Based")
                }
            }

            // Navigation buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onNavigateToHome,
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
                        text = "Explore Phobias",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onNavigateToProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "View Progress",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Take control of your fears today",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StatItem(number: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = number,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E3A8A)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    OneSmallStepTheme {
        MainScreen()
    }
}