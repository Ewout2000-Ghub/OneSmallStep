package com.example.onesmallstep.ui.progress

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.viewmodel.ProgressViewModel
import com.example.onesmallstep.viewmodel.ProgressViewModelFactory

class ProgressActivity : AppCompatActivity() {

    private lateinit var recyclerViewProgress: RecyclerView
    private lateinit var textViewTotalPhobias: TextView
    private lateinit var textViewCompletedSteps: TextView
    private lateinit var textViewCurrentStreak: TextView

    private val viewModel: ProgressViewModel by viewModels {
        ProgressViewModelFactory(
            PhobiaRepository(
                PhobiaDatabase.getDatabase(application).phobiaDao(),
                PhobiaDatabase.getDatabase(application).progressDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        initializeViews()
        setupRecyclerView()
        observeViewModel()
        loadProgressData()
    }

    private fun initializeViews() {
        recyclerViewProgress = findViewById(R.id.recyclerViewProgress)
        textViewTotalPhobias = findViewById(R.id.textViewTotalPhobias)
        textViewCompletedSteps = findViewById(R.id.textViewCompletedSteps)
        textViewCurrentStreak = findViewById(R.id.textViewCurrentStreak)
    }

    private fun setupRecyclerView() {
        // Create and set up progress adapter
        recyclerViewProgress.apply {
            layoutManager = LinearLayoutManager(this@ProgressActivity)
            // adapter = progressAdapter (implement based on your needs)
        }
    }

    private fun observeViewModel() {
        viewModel.activePhobias.observe(this, Observer { phobias ->
            textViewTotalPhobias.text = "Active Phobias: ${phobias.size}"
        })

        viewModel.completedSteps.observe(this, Observer { steps ->
            textViewCompletedSteps.text = "Completed Steps: ${steps.size}"
        })

        // Add more observers for streak data, progress charts, etc.
    }

    private fun loadProgressData() {
        viewModel.loadProgressData()
    }
}