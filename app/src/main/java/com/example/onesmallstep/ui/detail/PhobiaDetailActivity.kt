package com.example.onesmallstep.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.entities.ExposureLevel
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.ui.exposure.ExposureActivity
import com.example.onesmallstep.viewmodel.PhobiaDetailViewModel
import com.example.onesmallstep.viewmodel.PhobiaDetailViewModelFactory

class PhobiaDetailActivity : AppCompatActivity(), LevelAdapter.OnLevelClickListener {

    private lateinit var textTitle: TextView
    private lateinit var textDescription: TextView
    private lateinit var textReassurance: TextView
    private lateinit var recyclerViewLevels: RecyclerView
    private lateinit var levelAdapter: LevelAdapter

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
        setContentView(R.layout.activity_phobia_detail)

        val phobiaId = intent.getIntExtra("PHOBIA_ID", -1)
        if (phobiaId == -1) {
            finish()
            return
        }

        initializeViews()
        setupRecyclerView()
        observeViewModel()
        viewModel.loadPhobiaDetails(phobiaId)
    }

    private fun initializeViews() {
        textTitle = findViewById(R.id.textPhobiaTitle)
        textDescription = findViewById(R.id.textPhobiaDescription)
        textReassurance = findViewById(R.id.textReassurance)
        recyclerViewLevels = findViewById(R.id.recyclerViewLevels)
    }

    private fun setupRecyclerView() {
        levelAdapter = LevelAdapter(this)
        recyclerViewLevels.apply {
            layoutManager = LinearLayoutManager(this@PhobiaDetailActivity)
            adapter = levelAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.phobia.observe(this, Observer { phobia ->
            phobia?.let {
                textTitle.text = it.scientificName
                textDescription.text = it.description
                textReassurance.text = "This plan gradually helps you overcome ${it.name.lowercase()} at your own pace. Take your time with each level."
            }
        })

        viewModel.levels.observe(this, Observer { levels ->
            levelAdapter.submitList(levels)
        })
    }

    override fun onLevelClick(level: ExposureLevel) {
        val intent = Intent(this, ExposureActivity::class.java).apply {
            putExtra("LEVEL_ID", level.id)
            putExtra("PHOBIA_ID", level.phobiaId)
        }
        startActivity(intent)
    }
}