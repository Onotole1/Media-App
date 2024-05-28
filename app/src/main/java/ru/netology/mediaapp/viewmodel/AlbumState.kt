package ru.netology.mediaapp.viewmodel

data class AlbumState(
    val status: Status = Status.IDLE,
    val tracks: List<TrackUiModel> = emptyList(),
)

enum class Status {
    IDLE,
    ERROR,
    LOADING,
}