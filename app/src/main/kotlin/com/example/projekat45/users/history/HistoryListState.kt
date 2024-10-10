package com.example.projekat45.users.history

import com.example.projekat45.users.UsersData

interface HistoryListState {
    data class HistoryState(
        val usersData: UsersData,
    )
}