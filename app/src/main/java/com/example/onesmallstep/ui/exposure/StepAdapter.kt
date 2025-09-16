package com.example.onesmallstep.ui.exposure

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onesmallstep.R
import com.example.onesmallstep.data.entities.ExposureStep

class StepAdapter(
    private val listener: OnStepCompleteListener
) : ListAdapter<ExposureStep, StepAdapter.StepViewHolder>(StepDiffCallback()) {

    interface OnStepCompleteListener {
        fun onStepComplete(step: ExposureStep, anxietyRating: Int, notes: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exposure_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textStepTitle: TextView = itemView.findViewById(R.id.textStepTitle)
        private val textStepDescription: TextView = itemView.findViewById(R.id.textStepDescription)
        private val textStepInstructions: TextView = itemView.findViewById(R.id.textStepInstructions)
        private val textStepDuration: TextView = itemView.findViewById(R.id.textStepDuration)
        private val buttonComplete: Button = itemView.findViewById(R.id.buttonCompleteStep)
        private val checkboxCompleted: CheckBox = itemView.findViewById(R.id.checkboxCompleted)

        fun bind(step: ExposureStep) {
            textStepTitle.text = "Step ${step.stepNumber}: ${step.title}"
            textStepDescription.text = step.description
            textStepInstructions.text = step.instructions
            textStepDuration.text = "${step.duration} - ${step.frequency}"

            buttonComplete.setOnClickListener {
                showCompletionDialog(step)
            }
        }

        private fun showCompletionDialog(step: ExposureStep) {
            val dialogView = LayoutInflater.from(itemView.context)
                .inflate(R.layout.dialog_step_completion, null)

            val seekBarAnxiety = dialogView.findViewById<SeekBar>(R.id.seekBarAnxiety)
            val textAnxietyLevel = dialogView.findViewById<TextView>(R.id.textAnxietyLevel)
            val editTextNotes = dialogView.findViewById<EditText>(R.id.editTextNotes)

            seekBarAnxiety.max = 10
            seekBarAnxiety.progress = 5

            seekBarAnxiety.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    textAnxietyLevel.text = "Anxiety Level: $progress/10"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            AlertDialog.Builder(itemView.context)
                .setTitle("Complete Step")
                .setMessage("How did this step go?")
                .setView(dialogView)
                .setPositiveButton("Complete") { _, _ ->
                    val anxietyRating = seekBarAnxiety.progress
                    val notes = editTextNotes.text.toString().takeIf { it.isNotBlank() }
                    listener.onStepComplete(step, anxietyRating, notes)
                    checkboxCompleted.isChecked = true
                    buttonComplete.isEnabled = false
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private class StepDiffCallback : DiffUtil.ItemCallback<ExposureStep>() {
        override fun areItemsTheSame(oldItem: ExposureStep, newItem: ExposureStep): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExposureStep, newItem: ExposureStep): Boolean {
            return oldItem == newItem
        }
    }
}