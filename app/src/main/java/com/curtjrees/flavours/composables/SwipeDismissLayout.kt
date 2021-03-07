package com.curtjrees.flavours.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.abs
import kotlin.math.roundToInt

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
    val interactionState = remember { MutableInteractionSource() }

    val enabled = state.currentValue != SwipeDismissLayoutState.CLOSED
    val active = enabled || state.isAnimationRunning
    val beingDraggedState = interactionState.collectIsDraggedAsState()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val contentSize = remember { mutableStateOf(10000f) } //Large number to keep content offscreen initially
    val swipeAreaSize = with(LocalDensity.current) {
        val diff = abs(screenHeight.value - contentSize.value)
        diff.toDp().toPx()
    }
    val velocityThreshold = contentSize.value.dp / 4

    val anchors = mapOf(0f to SwipeDismissLayoutState.OPEN, swipeAreaSize to SwipeDismissLayoutState.CLOSED)
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
                velocityThreshold = velocityThreshold,
                thresholds = { _, _ -> FractionalThreshold(swipeThreshold) },
                orientation = Orientation.Vertical,
                resistance = SwipeableDefaults.resistanceConfig(anchors.keys, factorAtMin = 48f, factorAtMax = 48f),
            )
            .background(scrimBackground)
            .zIndex(zIndex)
    ) {

        //TODO: Figure out nested scrolling
        Column(
            Modifier
                .fillMaxSize()
                .onSizeChanged { contentSize.value = it.height.toFloat() }
//                .verticalScroll(rememberScrollState(), enabled = !beingDraggedState.value)
//                .verticalScroll(rememberScrollState(), enabled = true)
        ) {
            Row(Modifier.alpha(headerContentAlpha)) { headerContent() }
            Column(
                modifier = Modifier
                    .absoluteOffset {
                        IntOffset(x = 0, y = state.offset.value.roundToInt())
                    }
                    .alpha(contentAlpha)
                    .wrapContentSize(Alignment.TopCenter),
                content = { content(swipePercent) }
            )
        }
    }
}

