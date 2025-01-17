package com.example.projekat45.users

import kotlinx.serialization.Serializable

@Serializable
data class UsersData(
    val users: List<User>,
    val pick: Int
) {
    companion object{
        val EMPTY = UsersData(users= emptyList(),pick = -1)
    }
}