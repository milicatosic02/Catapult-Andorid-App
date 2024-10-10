package com.example.projekat45.navigation

import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projekat45.cats.details.breedsDetailsScreen
import com.example.projekat45.cats.gallery.breedsGalleryScreen
import com.example.projekat45.cats.gallery.photo.breedsPhotoScreen
import com.example.projekat45.cats.list.breeds
import com.example.projekat45.cats.login.loginScreen
import com.example.projekat45.cats.quiz.guessCat.guessCatScreen
import com.example.projekat45.cats.quiz.leaderboard.leaderboardScreen
import com.example.projekat45.cats.quiz.quizScreen
import com.example.projekat45.cats.quiz.result.resultScreen
import com.example.projekat45.users.edit.editScreen
import com.example.projekat45.users.history.historyScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = {
            slideInHorizontally(
                animationSpec = spring(),
                initialOffsetX = {it},
            )
        },
        exitTransition =  { scaleOut(targetScale = 0.75f) },
        popEnterTransition = { scaleIn(initialScale = 0.75f) },
        popExitTransition = { slideOutHorizontally { it } },
    ){
        loginScreen(
            route = "login",
            navController = navController,
//            arguments = listOf(
//                navArgument("username") { type = NavType.StringType },
//                navArgument("nickname") { type = NavType.StringType },
//                navArgument("email") { type = NavType.StringType })
        )
        breeds(
            route = "breeds",
            navController =navController,
            goToQuiz = {
                navController.navigate("quiz/guess-cat")
            },
        )

        breedsDetailsScreen(
            route ="breeds/{id}",
            navController = navController,
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
            })
        )

        breedsGalleryScreen(
            route = "images/{id}",
            navController = navController,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            }),
            onPhotoClicked = {id,photoIndex->
                navController.navigate(route = "photo/${id}/${photoIndex}")
            }
        )

        breedsPhotoScreen(
            route ="photo/{id}/{photoIndex}",
            navController = navController,
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
            }, navArgument("photoIndex"){
                type = IntType
            })
        )

        guessCatScreen(
            route = "quiz/guess-cat",
            navController = navController
        )
        resultScreen(
            route = "quiz/result/{category}/{result}",
            navController = navController,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.IntType
                }, navArgument("result") {
                    type = NavType.FloatType
                }  )
        )

        leaderboardScreen(
            route = "quiz/leaderboard/{category}",
            navController = navController,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.IntType
                }
            )
        )
        historyScreen(
            route = "history",
            navController = navController
        )
        editScreen(
            route = "user/edit",
            navController = navController
        )

    }

}inline val SavedStateHandle.breedsId: String
    get() = checkNotNull(get("id")) {"breedsId is mandatory"}

inline val SavedStateHandle.addNewUser: Boolean
    get() = get("addNewUser") ?: false

inline val SavedStateHandle.photoIndex: Int
    get() = checkNotNull(get("photoIndex")) {"photoIndex is mandatory"}

inline val SavedStateHandle.category: Int
    get() = checkNotNull(get("category")) {"category is mandatory"}
inline val SavedStateHandle.result: Float
    get() = checkNotNull(get("result")) {"result is mandatory"}
