package com.example.ottov1.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationSelectionDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun locationSelectionDialog_initialState_displaysCorrectly() {
        // Given
        val currentLocation = "Test Location"

        // When
        composeTestRule.setContent {
            LocationSelectionDialog(
                onDismiss = { },
                onConfirm = { },
                currentLocation = currentLocation
            )
        }

        // Then
        composeTestRule.onNodeWithText("Enter Location").assertExists()
        composeTestRule.onNodeWithText("Location").assertExists()
        composeTestRule.onNodeWithText("OK").assertExists()
        composeTestRule.onNodeWithText("Cancel").assertExists()
        composeTestRule.onNodeWithText(currentLocation).assertExists()
    }

    @Test
    fun locationSelectionDialog_emptyInitialState_displaysCorrectly() {
        // When
        composeTestRule.setContent {
            LocationSelectionDialog(
                onDismiss = { },
                onConfirm = { },
                currentLocation = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("Enter Location").assertExists()
        composeTestRule.onNodeWithText("Location").assertExists()
        composeTestRule.onNodeWithText("OK").assertExists()
        composeTestRule.onNodeWithText("Cancel").assertExists()
    }

    @Test
    fun locationSelectionDialog_confirmButton_updatesLocation() {
        // Given
        val newLocation = "New Location"
        var confirmedLocation: String? = null

        // When
        composeTestRule.setContent {
            LocationSelectionDialog(
                onDismiss = { },
                onConfirm = { confirmedLocation = it },
                currentLocation = null
            )
        }

        // Then
        composeTestRule.onNode(hasText("Location") and hasSetTextAction())
            .performTextInput(newLocation)
        composeTestRule.onNodeWithText("OK").performClick()

        assert(confirmedLocation == newLocation)
    }

    @Test
    fun locationSelectionDialog_dismissButton_closesDialog() {
        // Given
        var isDismissed = false

        // When
        composeTestRule.setContent {
            LocationSelectionDialog(
                onDismiss = { isDismissed = true },
                onConfirm = { },
                currentLocation = null
            )
        }

        // Then
        composeTestRule.onNodeWithText("Cancel").performClick()
        assert(isDismissed)
    }

    @Test
    fun locationSelectionDialog_searchIcon_exists() {
        // When
        composeTestRule.setContent {
            LocationSelectionDialog(
                onDismiss = { },
                onConfirm = { },
                currentLocation = null
            )
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Search location").assertExists()
    }
} 