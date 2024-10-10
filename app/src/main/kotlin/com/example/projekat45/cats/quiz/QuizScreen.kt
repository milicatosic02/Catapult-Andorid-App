package com.example.projekat45.cats.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.projekat45.core.AppIconButton

fun NavGraphBuilder.quizScreen(
    route: String,
    navController: NavController
) = composable(route = route) {

    Surface(
        tonalElevation = 1.dp
    ) {
        QuizScreen(
            onClose = { navController.navigateUp() },
            guessCatQuiz = {navController.navigate("quiz/guess-cat")},
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    onClose: () -> Unit,
    guessCatQuiz: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Quiz!", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    AppIconButton(imageVector = Icons.AutoMirrored.Filled.ArrowBack, onClick = onClose)
                }
            )
        },
        content = {
            Box(
                modifier = Modifier.padding(it),
                contentAlignment = Alignment.TopCenter
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .align(Alignment.Center)
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(40.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp)
                    ) {

                        Button(
                            onClick = { guessCatQuiz() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp) // Adjust button height for better visibility
                                .clip(RoundedCornerShape(8.dp)) // Rounded corners for button
                                .background(MaterialTheme.colorScheme.primary) // Primary color background
                                .padding(horizontal = 16.dp), // Padding for text inside button
                            shape = RoundedCornerShape(8.dp) // Rounded corners for button
                        ) {
                            Text(
                                text = "Start QUIZ!",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    letterSpacing = 1.sp // Increased letter spacing for emphasis
                                )
                            )
                        }

                    }

                }
            }
        }
    )
}