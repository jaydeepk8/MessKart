package com.example.messapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.messapp.data.source.MessDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    val messes = MessDataSource.getAllMesses()

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }
}
