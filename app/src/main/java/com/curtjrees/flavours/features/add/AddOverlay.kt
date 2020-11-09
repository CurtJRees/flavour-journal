package com.curtjrees.flavours.features.add

import androidx.annotation.Px
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animatedColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BaseTextField
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeableState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focusObserver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onSizeChanged
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.curtjrees.flavours.composables.SwipeDismissLayout
import com.curtjrees.flavours.composables.SwipeDismissLayoutState
import com.curtjrees.flavours.ui.FlavourJournalTheme


@ExperimentalFocus
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun AddOverlay(state: SwipeableState<SwipeDismissLayoutState>) {
    SwipeDismissLayout(
        state = state,
        headerContent = {
            IconButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    state.animateTo(SwipeDismissLayoutState.CLOSED)
                },
                icon = {
                    Icon(asset = Icons.Default.Close, tint = Color.White)
                }
            )
        },
        content = { swipePercent ->
            AddOverlayContent(swipePercent)
        }
    )
}

@ExperimentalFocus
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
@ExperimentalFoundationApi
//private fun AddOverlayContent() {
private fun AddOverlayContent(swipePercent: Float) {
    val columnPadding = 16.dp

    var titleInputValue by remember { mutableStateOf(TextFieldValue()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = columnPadding)
            .preferredHeightIn(min = 200.dp),
        backgroundColor = Color.White,
        content = {
            Column(Modifier.padding(16.dp)) {
                Text("9th November", fontSize = 12.sp)

                FlatTextField(
                    text = titleInputValue,
                    placeholderText = "Title",
                    modifier = Modifier.fillMaxWidth(),
                    onTextChange = { titleInputValue = it }
                )
            }
        }
    )

    Spacer(modifier = Modifier.fillMaxWidth().height(columnPadding))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = columnPadding)
            .preferredHeightIn(min = 100.dp),
        backgroundColor = Color.White,
        content = {
            Text("Other", color = Color.Black, fontSize = 24.sp)
        }
    )


    Spacer(modifier = Modifier.fillMaxWidth().height(64.dp))
}

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun FlatTextField(
    text: TextFieldValue,
    placeholderText: String? = null,
    textStyle: TextStyle = TextStyle.Default,
    onTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeIndicatorColor = Color.Red
    val inactiveIndicatorColor = Color.Gray
    val animatedIndicatorColor = animatedColor(initVal = inactiveIndicatorColor)

    fun updateState(active: Boolean) {
        val targetIndicatorColor = if (active) activeIndicatorColor else inactiveIndicatorColor
        animatedIndicatorColor.animateTo(targetIndicatorColor, anim = tween(durationMillis = 200, easing = LinearEasing))
    }


    @Px var textFieldHeightPx by remember { mutableStateOf(0) }
    val textFieldHeight = with(DensityAmbient.current) { textFieldHeightPx.toDp() }

    //TODO: Wait for single-line to be implemented

    Box(Modifier.onSizeChanged { textFieldHeightPx = it.height }) {
        if (placeholderText != null && text.text.isEmpty()) {
            Text(placeholderText, style = textStyle, modifier = modifier)
        }

        BaseTextField(
            value = text,
            textStyle = textStyle,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            onImeActionPerformed = { action ->
                if (action == ImeAction.Next) {

                }
            },
            modifier = modifier.focusObserver {
                updateState(it == FocusState.Active)
            },
            onValueChange = onTextChange,
        )

        Box(
            Modifier
                .height(textFieldHeight)
                .width(2.dp)
                .offset((-6).dp)
                .background(animatedIndicatorColor.value)
                .align(Alignment.CenterStart)
        )
    }
}


@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
@Preview
fun previewFlatTextField() {
    val textValue = TextFieldValue("Something")

    FlavourJournalTheme {
        FlatTextField(
            text = textValue,
            onTextChange = {}
        )
    }
}

@ExperimentalFocus
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview
@Composable
fun previewAddOverlay() {
    val addOverlayState = rememberSwipeableState(initialValue = SwipeDismissLayoutState.OPEN)

    FlavourJournalTheme {
        AddOverlay(addOverlayState)
    }
}