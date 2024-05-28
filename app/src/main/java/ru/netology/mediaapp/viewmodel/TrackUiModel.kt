package ru.netology.mediaapp.viewmodel

import ru.netology.mediaapp.model.Track

data class TrackUiModel(
    val file: String,
    val playing: Boolean = false,
) {

    companion object {
        fun fromModel(track: Track) = TrackUiModel(file = track.file)
    }
}
