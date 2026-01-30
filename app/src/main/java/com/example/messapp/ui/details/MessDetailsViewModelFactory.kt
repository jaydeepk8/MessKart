package com.example.messapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessDetailsViewModelFactory(
    private val messId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MessDetailsViewModel(messId) as T
    }
}
