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

        fun bind(phobia: Phobia) {
            textName.text = phobia.name
            textScientific.text = phobia.scientificName

            // Use only guaranteed Android built-in icons
            val iconRes = when (phobia.iconResource) {
                "spider" -> android.R.drawable.ic_menu_view
                "height" -> android.R.drawable.ic_menu_mylocation
                "airplane" -> android.R.drawable.ic_menu_send
                "microphone" -> android.R.drawable.ic_btn_speak_now
                "snake" -> android.R.drawable.ic_menu_zoom
                "dog" -> android.R.drawable.ic_menu_gallery
                "needle" -> android.R.drawable.ic_menu_edit
                "blood" -> android.R.drawable.ic_menu_close_clear_cancel
                "clown" -> android.R.drawable.ic_menu_camera
                "box" -> android.R.drawable.ic_menu_preferences
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