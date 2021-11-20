package com.ariel.readme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserProfileViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userId : String = savedStateHandle["uid"] ?:  throw IllegalArgumentException("missing user id")
}