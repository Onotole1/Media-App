package ru.netology.mediaapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.netology.mediaapp.adapter.TracksAdapter
import ru.netology.mediaapp.databinding.ActivityMainBinding
import ru.netology.mediaapp.viewmodel.AlbumViewModel
import ru.netology.mediaapp.viewmodel.Status
import ru.netology.mediaapp.viewmodel.Status.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel: AlbumViewModel by viewModels()

        val adapter = TracksAdapter {
            viewModel.play(it.file)
        }

        binding.content.adapter = adapter

        binding.retry.setOnClickListener {
            viewModel.load()
        }

        viewModel.state.flowWithLifecycle(lifecycle)
            .onEach {
                adapter.submitList(it.tracks)

                when (it.status) {
                    IDLE -> {
                        binding.progress.isGone = true
                        binding.errorGroup.isGone = true
                    }

                    ERROR -> {
                        binding.progress.isGone = true
                        binding.errorGroup.isVisible = true
                    }

                    LOADING -> {
                        binding.progress.isVisible = true
                        binding.errorGroup.isGone = true
                    }
                }
            }
            .launchIn(lifecycleScope)
    }
}