package com.curtjrees.flavours

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.curtjrees.flavours.composables.SwipeDismissLayoutState
import com.curtjrees.flavours.features.add.AddOverlay
import com.curtjrees.flavours.features.home.HomeScreen
import com.curtjrees.flavours.navigation.NavigationViewModel
import com.curtjrees.flavours.navigation.Screen
import com.curtjrees.flavours.ui.FlavourJournalTheme
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
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

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainContent(navigationViewModel: NavigationViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val addOverlayState = rememberSwipeableState(
        initialValue = SwipeDismissLayoutState.CLOSED,
        animationSpec = animationSpec
    )

    val navigationEventCallback: (Screen) -> Unit = {
        navigationViewModel.navigateTo(it)
    }

    fun openOverlay() {
        coroutineScope.launch {
            addOverlayState.animateTo(SwipeDismissLayoutState.OPEN)
        }
    }


    Box {
        Crossfade(navigationViewModel.currentScreen) { screen ->
            when (screen) {
                is Screen.Home -> HomeScreen(navigationEventCallback) {
                    openOverlay()
                }
            }
        }
        AddOverlay(addOverlayState)
    }
}