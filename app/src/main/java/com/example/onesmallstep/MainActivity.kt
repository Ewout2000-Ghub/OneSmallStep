// Fixed MainActivity.kt - This should be the main Compose activity
package com.example.onesmallstep

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.onesmallstep.ui.home.HomeActivity
import com.example.onesmallstep.ui.progress.ProgressActivity
import com.example.onesmallstep.ui.theme.OneSmallStepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "One Small Step",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Overcome your fears, one step at a time",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = onNavigateToHome,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Explore Phobias")
        }

        OutlinedButton(
            onClick = onNavigateToProgress,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Progress")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    OneSmallStepTheme {
        MainScreen()
    }
}