package com.example.projekat45.users.edit

import android.graphics.Bitmap
import android.widget.ImageView

interface EditListState {
    data class EditState (
        val nickname: String,
        val name: String,
        val email: String,
        val saveUserPassed: Boolean = false
    ) {
        sealed class DetailsError {
            data class DataUpdateFailed(val cause: Throwable? = null): DetailsError()
        }
    }

    sealed class EditUIEvent{
        data class NicknameInputChanged(val nickname: String) : EditUIEvent()
        data class EmailInputChanged(val email: String) : EditUIEvent()
        data class NameInputChanged(val name: String) :  EditUIEvent()
        data object SaveChanges :  EditUIEvent()
    }
}