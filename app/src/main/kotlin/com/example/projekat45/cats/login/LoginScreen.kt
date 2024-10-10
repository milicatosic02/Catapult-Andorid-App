package com.example.projekat45.cats.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.loginScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()
    val context = LocalContext.current
    Surface(
        tonalElevation = 1.dp
    ) {
        if (loginViewModel.hasAccount() || loginState.loginCheckPassed) {
            navController.navigate("breeds")
        }
        else {
            LoginScreen(
                loginState = loginState,
                onClick = { uiEvent ->
                    if (loginViewModel.isInfoValid()) {
                        loginViewModel.setLoginEvent(uiEvent)
                    }
                    else {
                        Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }
                },
                onValueChange = { uiEvent -> loginViewModel.setLoginEvent(uiEvent) }
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginState: LoginListState.LoginState,
    onClick: (uiEvent: LoginListState.LoginUIEvent) -> Unit,
    onValueChange: (uiEvent: LoginListState.LoginUIEvent) -> Unit
) {
    val radialFade = Brush.radialGradient(0.5f to Color.Green, 1f to Color.Transparent)
    Scaffold(
        content = { pv ->
            Column(
                modifier = Modifier
                    .padding(pv)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(text = "Hello, please enter your info", style = MaterialTheme.typography.headlineSmall)


                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        OutlinedTextField(
                            value = loginState.name,
                            onValueChange = {
                                onValueChange(
                                    LoginListState.LoginUIEvent.NameInputChanged(
                                        name = it
                                    )
                                )
                            },
                            label = { Text("Name") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = loginState.nickname,
                            onValueChange = {
                                onValueChange(
                                    LoginListState.LoginUIEvent.NicknameInputChanged(
                                        nickname = it
                                    )
                                )
                            },
                            label = { Text("Nickname") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = loginState.email,
                            onValueChange = {
                                onValueChange(
                                    LoginListState.LoginUIEvent.EmailInputChanged(
                                        email = it
                                    )
                                )
                            },
                            label = { Text("Email address") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Button(onClick = { onClick(LoginListState.LoginUIEvent.AddUser)}) {
                                Text(text = "Log In")
                            }
                        }
                    }
                }
            }
        }
    )

}
