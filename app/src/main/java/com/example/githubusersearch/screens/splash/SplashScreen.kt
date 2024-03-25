package com.example.githubusersearch.screens.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.githubusersearch.R
import com.example.githubusersearch.screens.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(splashViewModel: SplashViewModel, navController: NavController) {
    // Предположим, что useCase возвращает длительность анимаций
    when (val uiState = splashViewModel.uiState.collectAsState().value) {
        SplashViewModel.UiState.Empty -> {

        }

        is SplashViewModel.UiState.Error -> {

        }

        SplashViewModel.UiState.Loading -> {

        }

        is SplashViewModel.UiState.Success -> {
            // Состояние для анимации скейлинга
            var startScaling by remember { mutableStateOf(false) }
            var startRotating by remember { mutableStateOf(false) }
            var contentVisible by remember { mutableStateOf(true) }
            var navigateToNextScreen by remember { mutableStateOf(false) }

            val scale = animateFloatAsState(
                targetValue = if (startScaling) 1f else 0f, // Конечное значение масштаба
                animationSpec = tween(
                    durationMillis = uiState.duration.scaleDuration, // Продолжительность анимации 1 секунда
                    easing = LinearOutSlowInEasing // Эффект ускорения для более естественного движения
                ),
                finishedListener = {
                    startRotating = true
                }
            )

            val rotation by animateFloatAsState(
                targetValue = if (startRotating) 360f else 0f, // Начните вращение только после скейлинга
                animationSpec = tween(durationMillis = uiState.duration.rotationDuration),
                finishedListener = {
                    contentVisible = false // Скрываем контент после завершения анимации
                    navigateToNextScreen = true // Готовы к навигации
                }
            )

            LaunchedEffect(Unit) {
                startScaling = true
            }

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.bg_github),
                    contentDescription = "Splash Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                            rotationZ = rotation
                        }
                )
            }

            LaunchedEffect(navigateToNextScreen) {
                if (navigateToNextScreen) {
                    navController.navigate(Screens.USERS_LIST) {
                        popUpTo(Screens.SPLASH) { inclusive = true }
                    }
                }
            }


        }
    }
}
