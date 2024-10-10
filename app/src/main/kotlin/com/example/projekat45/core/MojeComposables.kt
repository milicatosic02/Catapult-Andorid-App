package com.example.projekat45.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null
) {
    IconButton(onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Breeds", style = MaterialTheme.typography.labelLarge)
        },
        navigationIcon = {
            AppIconButton(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBackClick)
        },
    )
}