package com.example.projekat45.cats.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat45.navigation.addNewUser
import com.example.projekat45.users.User
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val usersData: UsersDataStore
) : ViewModel() {

    private val addNewUser = savedStateHandle.addNewUser
    private val _loginState = MutableStateFlow(LoginListState.LoginState(addNewUser = addNewUser))
    val loginState = _loginState.asStateFlow()

    private val _loginEvents = MutableSharedFlow<LoginListState.LoginUIEvent>()

    private val NICKNAME_PATTERN = Regex("[A-Za-z0-9_]+")

    private fun setLoginState(updateWith: LoginListState.LoginState.() -> LoginListState.LoginState) =
        _loginState.getAndUpdate(updateWith)

    fun setLoginEvent(event: LoginListState.LoginUIEvent) = viewModelScope.launch {  _loginEvents.emit(event) }

    init {
        observerEvents()
    }



    private fun observerEvents() {
        viewModelScope.launch {
            _loginEvents.collect {
                when (it) {
                    is LoginListState.LoginUIEvent.EmailInputChanged -> emailChange(it.email)
                    is LoginListState.LoginUIEvent.NameInputChanged -> nameChange(it.name)
                    is LoginListState.LoginUIEvent.NicknameInputChanged -> nicknameChange(it.nickname)
                    is LoginListState.LoginUIEvent.AddUser -> addUser()
                }
            }
        }
    }

    fun isInfoValid(): Boolean {
        if (loginState.value.name.isEmpty())
            return false
        if (loginState.value.nickname.isEmpty() || !NICKNAME_PATTERN.matches(loginState.value.nickname))
            return false
        if (loginState.value.email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(loginState.value.email).matches())
            return false
        return true
    }

    private fun addUser() {
        val user = User(name = loginState.value.name, nickname = loginState.value.nickname, email = loginState.value.email)

        viewModelScope.launch {
            usersData.addUser(user)
            setLoginState { copy(loginCheckPassed = true) }
        }
    }


    private fun emailChange(email: String) {
        viewModelScope.launch {
            setLoginState { copy(email = email) }
        }
    }

    private fun nameChange(name: String) {
        viewModelScope.launch {
            setLoginState { copy(name = name) }
        }
    }

    private fun nicknameChange(nickname: String) {
        viewModelScope.launch {
            setLoginState { copy(nickname = nickname) }
        }
    }

    fun hasAccount(): Boolean {
        return usersData.data.value.pick != -1 && !loginState.value.addNewUser
    }
}
