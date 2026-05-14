package com.example.messapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.messapp.data.model.Mess
import com.example.messapp.data.source.MessDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val allMesses = MessDataSource.getAllMesses()

    val filteredMesses = _uiState.map { state ->
        allMesses
            .filter { mess -> mess.matchesSearch(state.searchQuery) }
            .filter { mess -> mess.matchesFilters(state.activeFilters) }
            .sortByOption(state.sortOption)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(searchQuery = query)
        }
    }

    fun toggleFilter(filter: String) {
        _uiState.update { state ->
            val updatedFilters = if (filter in state.activeFilters) {
                state.activeFilters - filter
            } else {
                state.activeFilters + filter
            }
            state.copy(activeFilters = updatedFilters)
        }
    }

    fun setSortOption(option: String) {
        _uiState.update { it.copy(sortOption = option) }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(activeFilters = emptySet(), sortOption = "Nearest")
        }
    }

    private fun Mess.matchesSearch(query: String): Boolean {
        if (query.isBlank()) return true
        val normalized = query.trim()
        return name.contains(normalized, ignoreCase = true) ||
                cuisine.contains(normalized, ignoreCase = true) ||
                tags.any { it.contains(normalized, ignoreCase = true) }
    }

    private fun Mess.matchesFilters(filters: Set<String>): Boolean {
        if (filters.isEmpty()) return true
        return filters.all { filter ->
            when (filter) {
                "Pure Veg" -> tags.any { it.contains("Pure Veg", ignoreCase = true) }
                "Veg / Non-Veg" -> tags.any { it.contains("Non", ignoreCase = true) }
                "Home Delivery" -> tags.any { it.contains("Home Delivery", ignoreCase = true) }
                "Dine-in" -> tags.any { it.contains("Dine", ignoreCase = true) }
                "Rating 4.5+" -> rating >= 4.5
                "Under 1 km" -> distanceValueKm() < 1.0
                else -> true
            }
        }
    }

    private fun List<Mess>.sortByOption(option: String): List<Mess> {
        return when (option) {
            "Top Rated" -> sortedByDescending { it.rating }
            "Fastest" -> sortedBy { it.timeMin.filter(Char::isDigit).toIntOrNull() ?: Int.MAX_VALUE }
            else -> sortedBy { it.distanceValueKm() }
        }
    }

    private fun Mess.distanceValueKm(): Double {
        return distanceKm.substringBefore(" ")
            .toDoubleOrNull() ?: Double.MAX_VALUE
    }
}
