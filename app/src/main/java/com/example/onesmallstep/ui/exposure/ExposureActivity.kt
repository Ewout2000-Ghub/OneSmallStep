package com.example.onesmallstep.ui.exposure

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.entities.ExposureStep
import com.example.onesmallstep.data.entities.UserProgress
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.viewmodel.ExposureViewModel
import com.example.onesmallstep.viewmodel.ExposureViewModelFactory
import java.util.*

class ExposureActivity : AppCompatActivity(), StepAdapter.OnStepCompleteListener {

    private lateinit var textLevelTitle: TextView
    private lateinit var textLevelInfo: TextView
    private lateinit var recyclerViewSteps: RecyclerView
    private lateinit var buttonMarkComplete: Button
    private lateinit var stepAdapter: StepAdapter

    private var levelId: Int = -1
    private var phobiaId: Int = -1

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
        setContentView(R.layout.activity_exposure)

        levelId = intent.getIntExtra("LEVEL_ID", -1)
        phobiaId = intent.getIntExtra("PHOBIA_ID", -1)

        if (levelId == -1 || phobiaId == -1) {
            finish()
            return
        }

        initializeViews()
        setupRecyclerView()
        observeViewModel()
        viewModel.loadStepsForLevel(levelId)
    }

    private fun initializeViews() {
        textLevelTitle = findViewById(R.id.textLevelTitle)
        textLevelInfo = findViewById(R.id.textLevelInfo)
        recyclerViewSteps = findViewById(R.id.recyclerViewSteps)
        buttonMarkComplete = findViewById(R.id.buttonMarkComplete)
    }

    private fun setupRecyclerView() {
        stepAdapter = StepAdapter(this)
        recyclerViewSteps.apply {
            layoutManager = LinearLayoutManager(this@ExposureActivity)
            adapter = stepAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.exposureLevel.observe(this, Observer { level ->
            level?.let {
                textLevelTitle.text = "Level ${it.levelNumber}: ${it.title}"
                textLevelInfo.text = it.description
            }
        })

        viewModel.steps.observe(this, Observer { steps ->
            stepAdapter.submitList(steps)
        })
    }

    override fun onStepComplete(step: ExposureStep, anxietyRating: Int, notes: String?) {
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
}