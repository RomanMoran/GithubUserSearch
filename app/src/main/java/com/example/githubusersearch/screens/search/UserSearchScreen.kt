package com.example.githubusersearch.screens.search


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import coil.compose.AsyncImage
import com.example.domain.models.GitHubUser

@ExperimentalMaterial3Api
@Composable
fun UserSearchScreen(userSearchViewModel: UserSearchViewModel, navController: NavController) {
    // State for the text field
    var textState by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = textState,
            onValueChange = {
                textState = it
                userSearchViewModel.searchUsers(textState)
            },
            label = { Text("Search GitHub Users") },
            modifier = Modifier.testTag("SearchGitHubUsersTextField") // Добавляем тег
        )

        // Existing when statement for displaying loading, success, error, or empty states
        when (val uiState = userSearchViewModel.uiState.collectAsState().value) {
            is UserSearchViewModel.UiState.Loading -> CircularProgressIndicator()
            is UserSearchViewModel.UiState.Success -> {
                if (uiState.users.isEmpty()) {
                    Text(text = "List is empty. Try something another")
                } else {
                    UserList(users = uiState.users, navController)
                }
            }

            is UserSearchViewModel.UiState.Error -> Text(text = uiState.message)
            UserSearchViewModel.UiState.Empty -> Text(text = "Please enter a search term.")
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