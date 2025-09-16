package com.example.onesmallstep.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.entities.ExposureLevel

class LevelAdapter(
    private val listener: OnLevelClickListener
) : ListAdapter<ExposureLevel, LevelAdapter.LevelViewHolder>(LevelDiffCallback()) {

    interface OnLevelClickListener {
        fun onLevelClick(level: ExposureLevel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exposure_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.textLevelTitle)
        private val textDescription: TextView = itemView.findViewById(R.id.textLevelDescription)
        private val textDuration: TextView = itemView.findViewById(R.id.textLevelDuration)

        fun bind(level: ExposureLevel) {
            textTitle.text = "Level ${level.levelNumber}: ${level.title}"
            textDescription.text = level.description
            textDuration.text = "Duration: ${level.estimatedDuration}"

            itemView.setOnClickListener {
                listener.onLevelClick(level)
            }
        }
    }

    private class LevelDiffCallback : DiffUtil.ItemCallback<ExposureLevel>() {
        override fun areItemsTheSame(oldItem: ExposureLevel, newItem: ExposureLevel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExposureLevel, newItem: ExposureLevel): Boolean {
            return oldItem == newItem
        }
    }
}