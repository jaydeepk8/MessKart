package com.example.messapp.ui.details

import androidx.lifecycle.ViewModel
import com.example.messapp.data.model.Mess
import com.example.messapp.data.source.MessDataSource

class MessDetailsViewModel(
    messId: Int
) : ViewModel() {

    val mess: Mess = MessDataSource.getMessById(messId)
}
