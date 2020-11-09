package com.curtjrees.flavours.composables

import androidx.compose.foundation.Interaction
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onSizeChanged
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.abs

enum class SwipeDismissLayoutState {
    OPEN,
    CLOSED,
}

@Composable
@ExperimentalMaterialApi
fun SwipeDismissLayout(
    state: SwipeableState<SwipeDismissLayoutState>,
    swipeThreshold: Float = 0.33f,
    scrimColor: Color = Color.Black,
    maxScrimAlpha: Float = 1f,
    maxZIndex: Float = 20f,
    content: @Composable ColumnScope.(Float) -> Unit,
    headerContent: @Composable RowScope.() -> Unit = {}
) {
    val interactionState = remember { InteractionState() }

    val enabled = state.value != SwipeDismissLayoutState.CLOSED
    val active = enabled || state.isAnimationRunning
    val beingDragged = Interaction.Dragged in interactionState

    val screenHeight = ConfigurationAmbient.current.screenHeightDp.dp
    val contentSize =
        remember { mutableStateOf(10000f) } //Large number to keep content offscreen initially
    val swipeAreaSize = with(DensityAmbient.current) {
        val diff = abs(screenHeight.value - contentSize.value)
        diff.toDp().toPx()
    }
    val velocityThreshold = contentSize.value.dp / 4

    val anchors =
        mapOf(0f to SwipeDismissLayoutState.OPEN, swipeAreaSize to SwipeDismissLayoutState.CLOSED)
    val swipeAmountPx = if (state.offset.value.isNaN().not()) state.offset.value else 0f
    val swipePercent = (1 - (swipeAreaSize - swipeAmountPx) / swipeAreaSize)

    val contentAlpha = if (active) (1 - swipePercent).coerceIn(0f, 1f) else 0f
    val zIndex = if (active) (maxZIndex * contentAlpha) else -1f
    val headerContentAlpha = contentAlpha - ((1 - contentAlpha) * 10)

    val scrimBackground = scrimColor.copy(alpha = contentAlpha * maxScrimAlpha)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .swipeable(
                state = state,
                enabled = enabled,
                anchors = anchors,
                interactionState = interactionState,
                velocityThreshold = velocityThreshold,
                thresholds = { _, _ -> FractionalThreshold(swipeThreshold) },
                orientation = Orientation.Vertical,
                resistance = SwipeableConstants.defaultResistanceConfig(
                    anchors = anchors.keys,
                    factorAtMin = 48f,
                    factorAtMax = 48f
                )
            )
            .background(scrimBackground)
            .zIndex(zIndex)
    ) {
        ScrollableColumn(
            isScrollEnabled = !beingDragged,
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { contentSize.value = it.height.toFloat() }
        ) {
            Row(
                modifier = Modifier.drawOpacity(headerContentAlpha),
                children = {
                    headerContent()
                }
            )
            Column(
                modifier = Modifier
                    .offsetPx(y = state.offset)
                    .drawOpacity(contentAlpha)
                    .wrapContentSize(Alignment.TopCenter),
                children = {
                    content(swipePercent)
                }
            )
        }
    }
}

