package com.example.githubusersearch

import com.example.domain.models.GitHubUser
import com.example.domain.usecases.search_users.SearchUsersUseCase
import com.example.githubusersearch.screens.search.UserSearchViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class MainViewModelTest {

    // Mock dependencies
    private val mockUseCase = mock(SearchUsersUseCase::class.java)

    // Instantiate ViewModel
    private lateinit var viewModel: UserSearchViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Set the main coroutines dispatcher for unit testing
        Dispatchers.setMain(UnconfinedTestDispatcher())

        // Initialize ViewModel before each test
        viewModel = UserSearchViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        // Reset the main coroutines dispatcher to the original Main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `search users, valid query, updates state to success`() = runTest {
        // Define expected results
        val users = listOf(GitHubUser.createMockedInstance())
        // Mock use case response
        whenever(mockUseCase.invoke(anyString())).thenReturn(flowOf(Result.success(users)))

        // Act
        viewModel.searchUsers("query")

        // Даем время на асинхронное выполнение и обновление состояния
        advanceUntilIdle()

        // Assert
        val lastState = viewModel.uiState.value
        assertTrue("Expected Success state but was $lastState", lastState is UserSearchViewModel.UiState.Success && lastState.users == users)

    }

    @Test
    fun `search users with empty query, updates state to empty`() = runTest {
        // Act
        viewModel.searchUsers("")
        // Даем время на асинхронное выполнение и обновление состояния
        advanceUntilIdle()

        // Assert
        assertEquals(UserSearchViewModel.UiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `search users fails, updates state to error`() = runTest {
        val errorMessage = "Error fetching users"
        // Mock use case response to throw an exception
        whenever(mockUseCase.invoke(anyString())).thenReturn(flow { throw Exception(errorMessage) })

        // Act
        viewModel.searchUsers("query")

        // Wait for all coroutines to complete
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.uiState.value is UserSearchViewModel.UiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as UserSearchViewModel.UiState.Error).message)
    }

    @Test
    fun `search users started, before executing query`() = runTest {
        // Создаем CompletableDeferred для контроля выполнения запроса
        val deferredResult = CompletableDeferred<Result<List<GitHubUser>>>()

        // Настраиваем мок, чтобы он возвращал "зависший" deferredResult
        whenever(mockUseCase.invoke(anyString())).thenReturn(flow { deferredResult.await() })

        // Запускаем поиск, который не должен завершиться, пока мы явно не разрешим
        viewModel.searchUsers("query")

        // Проверяем, что состояние было обновлено до Loading
        assertTrue(viewModel.uiState.value is UserSearchViewModel.UiState.Loading)

        // Завершаем операцию, чтобы не блокировать тест навсегда
        deferredResult.complete(Result.success(listOf(GitHubUser.createMockedInstance())))
    }




}