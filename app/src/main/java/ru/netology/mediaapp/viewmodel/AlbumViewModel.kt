package ru.netology.mediaapp.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.netology.mediaapp.api.AlbumApi
import ru.netology.mediaapp.api.BASE_URL

class AlbumViewModel : ViewModel() {

    private val api = AlbumApi.AlbumApiService.api
    private val _state = MutableStateFlow(AlbumState())
    private val mediaPlayer = MediaPlayer()
    val state = _state.asStateFlow()

    init {
        load()

        mediaPlayer.setOnCompletionListener {
            val tracks = _state.value.tracks
            val played = tracks.indexOfFirst { it.playing }

            val nextIndex = played + 1
            val safeIndex = if (nextIndex > tracks.size) {
                0
            } else {
                nextIndex
            }

            play(tracks[safeIndex].file)
        }
    }

    fun play(file: String) {
        _state.update { state ->
            state.copy(
                tracks = state.tracks.map {
                    if (it.file == file) {
                        val playing = it.playing
                        if (playing) {
                            mediaPlayer.stop()
                        } else {
                            playInternal(file)
                        }
                        it.copy(playing = !it.playing)
                    } else {
                        it.copy(playing = false)
                    }
                }
            )
        }
    }

    private fun playInternal(file: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource("${BASE_URL}$file")
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
        mediaPlayer.prepareAsync()
    }

    fun load() {
        _state.update {
            it.copy(status = Status.LOADING)
        }

        viewModelScope.launch {
            runCatching {
                api.getAllTracks()
            }
                .onFailure {
                    _state.update {
                        it.copy(status = Status.ERROR)
                    }
                }
                .onSuccess { album ->
                    _state.update {
                        it.copy(
                            status = Status.IDLE,
                            tracks = album.tracks.map(TrackUiModel::fromModel),
                        )
                    }
                }
        }
    }
}