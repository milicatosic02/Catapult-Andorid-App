package com.example.projekat45.users.edit

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import java.io.File
import java.io.FileOutputStream
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val usersData: UsersDataStore
) : ViewModel() {

    private val _editState = MutableStateFlow(
        EditListState.EditState(
            name = usersData.data.value.users[usersData.data.value.pick].name,
            nickname = usersData.data.value.users[usersData.data.value.pick].nickname,
            email = usersData.data.value.users[usersData.data.value.pick].email,
        )
    )
    val editState = _editState.asStateFlow()

    private val _editEvents = MutableSharedFlow<EditListState.EditUIEvent>()

    private fun setEditState(updateWith: EditListState.EditState.() -> EditListState.EditState) =
        _editState.getAndUpdate(updateWith)

    fun setEditEvent(event: EditListState.EditUIEvent) =
        viewModelScope.launch { _editEvents.emit(event) }

    init {
        observerEvents()
    }


    private fun observerEvents() {
        viewModelScope.launch {
            _editEvents.collect {
                when (it) {
                    is EditListState.EditUIEvent.EmailInputChanged -> emailChange(it.email)
                    is EditListState.EditUIEvent.NameInputChanged -> nameChange(it.name)
                    is EditListState.EditUIEvent.NicknameInputChanged -> nicknameChange(it.nickname)
                    is EditListState.EditUIEvent.SaveChanges -> updateUser()
                }
            }
        }
    }


    fun isInfoValid(): Boolean {
        if (editState.value.name.isEmpty())
            return false
        if (editState.value.nickname.isEmpty())
            return false
        if (editState.value.email.isEmpty())
            return false
        return true
    }

    private fun updateUser() {
        val users = usersData.data.value.users.toMutableList()
        val pick = usersData.data.value.pick

        users[pick] = users[pick].copy(
            name = editState.value.name,
            nickname = editState.value.nickname,
            email = editState.value.email,
        )

        viewModelScope.launch {
            usersData.updateUser(users.toImmutableList())
            setEditState { copy(saveUserPassed = true) }
        }
    }


    private fun emailChange(email: String) {
        viewModelScope.launch {
            setEditState { copy(email = email) }
        }
    }

    private fun nameChange(name: String) {
        viewModelScope.launch {
            setEditState { copy(name = name) }
        }
    }

    private fun nicknameChange(nickname: String) {
        viewModelScope.launch {
            setEditState { copy(nickname = nickname) }
        }
    }
}