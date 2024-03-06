package com.example.githubusersearch

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import com.example.githubusersearch.main.MainActivity
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testUserSearchTextFieldExists() {
        // Проверяем, что TextField присутствует
        composeTestRule.onNodeWithTag("SearchGitHubUsersTextField", useUnmergedTree = true).assertExists()

        // Имитируем ввод текста
        composeTestRule.onNodeWithTag("SearchGitHubUsersTextField", useUnmergedTree = true).performTextInput("test")

        // Проверяем, что текст был введену
        composeTestRule.onNodeWithTag("SearchGitHubUsersTextField", useUnmergedTree = true).assertTextEquals("test")
    }

}
