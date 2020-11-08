package com.curtjrees.flavours

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.curtjrees.flavours.features.home.HomeScreen
import com.curtjrees.flavours.navigation.NavigationViewModel
import com.curtjrees.flavours.navigation.Screen
import com.curtjrees.flavours.ui.FlavourJournalTheme

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    private val navigationViewModel = NavigationViewModel() //TODO: Better construction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlavourJournalTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainContent(navigationViewModel)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MainContent(navigationViewModel: NavigationViewModel) {
    var showAddOverlay by remember { mutableStateOf(false) }

    val navigationEventCallback: (Screen) -> Unit = {
        navigationViewModel.navigateTo(it)
    }

    Box() {
        Crossfade(navigationViewModel.currentScreen) { screen ->
            when (screen) {
                is Screen.Home -> HomeScreen(navigationEventCallback) {
                    showAddOverlay = true
                }
            }
        }
        AddOverlay(showAddOverlay) {
            showAddOverlay = false
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AddOverlay(visible: Boolean, onCloseCallback: () -> Unit) {

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
        content = {
            Card(
                backgroundColor = Color.Green,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clickable(onClick = onCloseCallback),
                content = {

                }
            )
        }
    )

}