package com.example.ottov1.ui.addedit

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ottov1.data.model.ClimbingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ActivityFormContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockActivity = ClimbingActivity(
        id = 1L,
        name = "Test Activity",
        description = "Test Description",
        type = ActivityType.BOULDER,
        date = System.currentTimeMillis(),
        startHour = 10,
        startMinute = 30,
        endHour = 12,
        endMinute = 0,
        location = "Test Location",
        grade = "7a",
        minPeople = 2,
        maxPeople = 4
    )

    private val availableGrades = listOf("6a", "6b", "6c", "7a", "7b", "7c", "8a", "8b", "8c", "9")

    @Test
    fun nameField_initialState_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("name_field")
            .assertExists()
            .assertIsEnabled()
            .assertTextContains("Test Activity")
    }

    @Test
    fun nameField_showsError_whenEmpty() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity.copy(name = ""),
                isNameError = true,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("name_field")
            .assertExists()
            .assertIsEnabled()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Name is required")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun descriptionField_initialState_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("description_field")
            .assertExists()
            .assertIsEnabled()
            .assertTextContains("Test Description")
    }

    @Test
    fun activityTypeField_initialState_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("activity_type_field")
            .assertExists()
            .assertTextContains("Boulder")
    }

    @Test
    fun gradeField_initialState_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("grade_field")
            .assertExists()
            .assertTextContains("7a")
    }

    @Test
    fun locationField_initialState_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("location_field")
            .assertExists()
            .assertTextContains("Test Location")
    }

    @Test
    fun timeFields_initialState_displayCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("start_time_field")
            .assertExists()
            .assertTextContains("10:30")

        composeTestRule.onNodeWithTag("end_time_field")
            .assertExists()
            .assertTextContains("12:00")
    }

    @Test
    fun dateField_initialState_displaysCorrectly() {
        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = {},
                onDescriptionChange = {},
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("date_field")
            .assertExists()
    }

    @Test
    fun allInputFields_areInteractive() {
        var nameChanged = false
        var descriptionChanged = false

        composeTestRule.setContent {
            ActivityFormContent(
                modifier = androidx.compose.ui.Modifier,
                activity = mockActivity,
                isNameError = false,
                onNameChange = { nameChanged = true },
                onDescriptionChange = { descriptionChanged = true },
                onActivityTypeChange = {},
                onDateChange = {},
                onStartTimeChange = { _, _ -> },
                onEndTimeChange = { _, _ -> },
                onLocationChange = {},
                onGradeChange = {},
                onPeopleChange = { _, _ -> },
                availableGrades = availableGrades,
                showDatePicker = mutableStateOf(false),
                showStartTimePicker = mutableStateOf(false),
                showEndTimePicker = mutableStateOf(false),
                showLocationDialog = mutableStateOf(false),
                showMapDialog = mutableStateOf(false)
            )
        }

        composeTestRule.onNodeWithTag("name_field")
            .performTextInput("New")
        assert(nameChanged)

        composeTestRule.onNodeWithTag("description_field")
            .performTextInput("New")
        assert(descriptionChanged)
    }
} 