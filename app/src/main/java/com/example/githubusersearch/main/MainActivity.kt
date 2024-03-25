package com.example.githubusersearch.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.githubusersearch.screens.Screens
import com.example.githubusersearch.screens.search.UserSearchViewModel
import com.example.githubusersearch.screens.search.UserSearchScreen
import com.example.githubusersearch.screens.splash.SplashScreen
import com.example.githubusersearch.screens.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModel()
    private val userSearchViewModel: UserSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // todo how to organize not pass viewmodel and create it in screen
                AppNavigationContent(splashViewModel, userSearchViewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationContent(
    splashViewModel: SplashViewModel,
    userSearchViewModel: UserSearchViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SPLASH,
        enterTransition = {
            // Определите анимацию появления для входящего экрана
            fadeIn(animationSpec = tween(1500))
        },
        exitTransition =  { fadeOut(animationSpec = tween(1500)) } ,
    ) {
        composable(Screens.SPLASH) {
            SplashScreen(splashViewModel = splashViewModel, navController = navController)
        }
        composable(Screens.USERS_LIST) {
            UserSearchScreen(
                userSearchViewModel = userSearchViewModel,
                navController = navController
            )
        }
        composable(
            Screens.USERS_DETAIL,
            arguments = listOf(navArgument(Screens.USERS_DETAIL_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            UserDetailScreen(
                userLogin = backStackEntry.arguments?.getString(Screens.USERS_DETAIL_ARG) ?: ""
            )
        }
    }
}

@Composable
fun UserDetailScreen(userLogin: String) {
    // Layout for user details
    Text(text = "User details for $userLogin")
}