package ru.netology.mediaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mediaapp.R
import ru.netology.mediaapp.databinding.ItemTrackBinding
import ru.netology.mediaapp.viewmodel.TrackUiModel

class TracksAdapter(
    private val onPlayClickListener: (TrackUiModel) -> Unit,
) : ListAdapter<TrackUiModel, TracksAdapter.TrackViewHolder>(TrackDiffItemCallback) {

    class TrackViewHolder(
        private val onPlayClickListener: (TrackUiModel) -> Unit,
        private val binding: ItemTrackBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trackUiModel: TrackUiModel) {
            binding.play.setOnClickListener {
                onPlayClickListener(trackUiModel)
            }
            binding.trackName.text = trackUiModel.file
            binding.play.setIconResource(
                if (trackUiModel.playing) {
                    R.drawable.pause_24
                } else {
                    R.drawable.play_24
                }
            )
        }
    }

    object TrackDiffItemCallback : DiffUtil.ItemCallback<TrackUiModel>() {
        override fun areItemsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel): Boolean =
            oldItem.file == newItem.file

        override fun areContentsTheSame(oldItem: TrackUiModel, newItem: TrackUiModel): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            onPlayClickListener,
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}