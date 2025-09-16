package com.example.onesmallstep.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.entities.Phobia

class PhobiaAdapter(
    private val listener: OnPhobiaClickListener
) : ListAdapter<Phobia, PhobiaAdapter.PhobiaViewHolder>(PhobiaDiffCallback()) {

    interface OnPhobiaClickListener {
        fun onPhobiaClick(phobia: Phobia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhobiaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_phobia_card, parent, false)
        return PhobiaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhobiaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhobiaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageIcon: ImageView = itemView.findViewById(R.id.imagePhobiaIcon)
        private val textName: TextView = itemView.findViewById(R.id.textPhobiaName)
        private val textScientific: TextView = itemView.findViewById(R.id.textScientificName)
        private val textCategoryBadge: TextView = itemView.findViewById(R.id.textCategoryBadge)

        fun bind(phobia: Phobia) {
            textName.text = phobia.name
            textScientific.text = phobia.scientificName

            // Set category badge
            textCategoryBadge.text = when (phobia.category) {
                "common" -> "Common"
                "rare" -> "Specific"
                else -> "Other"
            }

            // Enhanced icon mapping with more phobias
            val iconRes = when (phobia.iconResource) {
                // Common phobias
                "spider" -> android.R.drawable.ic_menu_view
                "height" -> android.R.drawable.ic_menu_mylocation
                "airplane" -> android.R.drawable.ic_menu_send
                "microphone" -> android.R.drawable.ic_btn_speak_now
                "snake" -> android.R.drawable.ic_menu_zoom
                "dog" -> android.R.drawable.ic_menu_gallery
                "needle" -> android.R.drawable.ic_menu_edit
                "blood" -> android.R.drawable.ic_menu_close_clear_cancel
                "box" -> android.R.drawable.ic_menu_preferences
                "people" -> android.R.drawable.ic_menu_sort_by_size

                // Additional common phobias
                "stomach" -> android.R.drawable.ic_menu_help
                "car" -> android.R.drawable.ic_menu_directions
                "bacteria" -> android.R.drawable.ic_menu_search
                "moon" -> android.R.drawable.ic_menu_today
                "storm" -> android.R.drawable.ic_menu_share

                // Rare/specific phobias
                "clown" -> android.R.drawable.ic_menu_camera
                "doll" -> android.R.drawable.ic_menu_gallery
                "holes" -> android.R.drawable.ic_menu_view
                "mirror" -> android.R.drawable.ic_menu_crop
                "balloon" -> android.R.drawable.ic_menu_compass
                "dentist" -> android.R.drawable.ic_menu_manage
                "throat" -> android.R.drawable.ic_menu_call
                "hand" -> android.R.drawable.ic_menu_agenda
                "field" -> android.R.drawable.ic_menu_mapmode
                "skull" -> android.R.drawable.ic_menu_info_details

                else -> android.R.drawable.ic_dialog_info
            }
            imageIcon.setImageResource(iconRes)

            itemView.setOnClickListener {
                listener.onPhobiaClick(phobia)
            }
        }
    }

    private class PhobiaDiffCallback : DiffUtil.ItemCallback<Phobia>() {
        override fun areItemsTheSame(oldItem: Phobia, newItem: Phobia): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Phobia, newItem: Phobia): Boolean {
            return oldItem == newItem
        }
    }
}