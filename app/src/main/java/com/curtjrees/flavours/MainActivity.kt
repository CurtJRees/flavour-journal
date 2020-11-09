package com.curtjrees.flavours

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.setContent
import com.curtjrees.flavours.composables.SwipeDismissLayoutState
import com.curtjrees.flavours.features.add.AddOverlay
import com.curtjrees.flavours.features.home.HomeScreen
import com.curtjrees.flavours.navigation.NavigationViewModel
import com.curtjrees.flavours.navigation.Screen
import com.curtjrees.flavours.ui.FlavourJournalTheme

@ExperimentalFocus
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
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



private val animationSpec: AnimationSpec<Float> = tween(durationMillis = 400, easing = FastOutSlowInEasing)

@ExperimentalFocus
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun MainContent(navigationViewModel: NavigationViewModel) {
    val addOverlayState = rememberSwipeableState(
        initialValue = SwipeDismissLayoutState.CLOSED,
        animationSpec = animationSpec
    )

    val navigationEventCallback: (Screen) -> Unit = {
        navigationViewModel.navigateTo(it)
    }

    Box() {
        Crossfade(navigationViewModel.currentScreen) { screen ->
            when (screen) {
                is Screen.Home -> HomeScreen(navigationEventCallback) {
                    addOverlayState.animateTo(SwipeDismissLayoutState.OPEN)
                }
            }
        }
        AddOverlay(addOverlayState)
    }
}