@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.githubusersearch.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.domain.models.GitHubUser
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigationContent(mainViewModel = mainViewModel)
            }
        }
    }
}


@Composable
fun AppNavigationContent(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            UserSearchScreen(viewModel = mainViewModel, navController = navController)
        }
        composable(
            "userDetail/{login}",
            arguments = listOf(navArgument("login") { type = NavType.StringType })
        ) { backStackEntry ->
            UserDetailScreen(userLogin = backStackEntry.arguments?.getString("login") ?: "")
        }
    }
}

@Composable
fun UserSearchScreen(viewModel: MainViewModel, navController: NavController) {
    // State for the text field
    var textState by remember { mutableStateOf("") }

    Column {
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                viewModel.searchUsers(textState)
            },
            label = { Text("Search GitHub Users") },
            modifier = Modifier.testTag("SearchGitHubUsersTextField") // Добавляем тег
        )

        // Existing when statement for displaying loading, success, error, or empty states
        when (val uiState = viewModel.uiState.collectAsState().value) {
            is MainViewModel.UiState.Loading -> CircularProgressIndicator()
            is MainViewModel.UiState.Success -> {
                if (uiState.users.isEmpty()) {
                    Text(text = "List is empty. Try something another")
                } else {
                    UserList(users = uiState.users, navController)
                }
            }

            is MainViewModel.UiState.Error -> Text(text = uiState.message)
            MainViewModel.UiState.Empty -> Text(text = "Please enter a search term.")
        }
    }
}

@Composable
fun UserList(users: List<GitHubUser>, navController: NavController) {
    LazyColumn {
        items(users.size) { index ->
            UserItem(user = users[index], navController)
        }
    }
}

@Composable
fun UserItem(user: GitHubUser, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate("userDetail/${user.login}")
            }) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "Translated description of what the image contains"
        )
        Text(
            text = user.login,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )

    }
}

@Composable
fun UserDetailScreen(userLogin: String) {
    // Layout for user details
    Text(text = "User details for $userLogin")
}