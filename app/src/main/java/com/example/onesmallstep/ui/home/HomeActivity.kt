package com.example.onesmallstep.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.database.PhobiaDatabase
import com.example.onesmallstep.data.entities.Phobia
import com.example.onesmallstep.data.repository.PhobiaRepository
import com.example.onesmallstep.ui.detail.PhobiaDetailActivity
import com.example.onesmallstep.viewmodel.HomeViewModel
import com.example.onesmallstep.viewmodel.HomeViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class HomeActivity : AppCompatActivity(), PhobiaAdapter.OnPhobiaClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var chipGroup: ChipGroup
    private lateinit var phobiaAdapter: PhobiaAdapter

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
        setContentView(R.layout.activity_home)

        initializeViews()
        setupRecyclerView()
        setupSearch()
        setupFilters()
        observeViewModel()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewPhobias)
        searchView = findViewById(R.id.searchView)
        chipGroup = findViewById(R.id.chipGroupFilters)
    }

    private fun setupRecyclerView() {
        phobiaAdapter = PhobiaAdapter(this)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = phobiaAdapter
        }
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchPhobias(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.loadAllPhobias()
                } else {
                    viewModel.searchPhobias(newText)
                }
                return true
            }
        })
    }

    private fun setupFilters() {
        val commonChip = findViewById<Chip>(R.id.chipCommon)
        val rareChip = findViewById<Chip>(R.id.chipRare)
        val allChip = findViewById<Chip>(R.id.chipAll)

        commonChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.loadPhobiasByCategory("common")
        }

        rareChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.loadPhobiasByCategory("rare")
        }

        allChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.loadAllPhobias()
        }
    }

    private fun observeViewModel() {
        viewModel.phobias.observe(this, Observer { phobias ->
            phobiaAdapter.submitList(phobias)
        })
    }

    override fun onPhobiaClick(phobia: Phobia) {
        val intent = Intent(this, PhobiaDetailActivity::class.java).apply {
            putExtra("PHOBIA_ID", phobia.id)
        }
        startActivity(intent)
    }
}