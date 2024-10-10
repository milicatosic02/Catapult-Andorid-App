package com.example.projekat45

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projekat45.navigation.AppNavigation
import com.example.projekat45.ui.theme.Projekat45Theme
import com.example.projekat45.users.UsersDataStore
import dagger.hilt.android.AndroidEntryPoint
import rs.edu.raf.rma.analytics.AppAnalytics
import rs.edu.raf.rma.core.theme.AppTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val analytics = AppAnalytics()
    @Inject
    lateinit var usersDataStore: UsersDataStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val usersData by usersDataStore.data.collectAsState()
                AppTheme(darkTheme = if(usersData.pick == -1) false else usersData.users[usersData.pick].darkTheme) {
                    AppNavigation()
                }

        }
    }
}

val LocalAnalytics = compositionLocalOf<AppAnalytics> {
    error("Analytics not provided")
}