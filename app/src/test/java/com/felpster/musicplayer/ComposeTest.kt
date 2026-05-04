package com.felpster.musicplayer


import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.robolectric.shadows.ShadowLog

/**
 * This class can be extended from the compose tests to initiate the composeTestRule.
 * It also provides a nice text representation of the UI tree in the console output
 * where we run the tests, to use it we need to call the printToLog method
 * on a composeTestRule node.
 */
open class ComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun shadowLog() {
        ShadowLog.stream = System.out
    }
}