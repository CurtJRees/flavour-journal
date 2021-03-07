package com.curtjrees.flavours.features.add

import androidx.annotation.Px
import androidx.compose.animation.Animatable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.curtjrees.flavours.composables.SwipeDismissLayout
import com.curtjrees.flavours.composables.SwipeDismissLayoutState
import com.curtjrees.flavours.ui.FlavourJournalTheme
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun AddOverlay(state: SwipeableState<SwipeDismissLayoutState>) {

    val coroutineScope = rememberCoroutineScope()

    fun close() {
        coroutineScope.launch {
            state.animateTo(SwipeDismissLayoutState.CLOSED)
        }
    }

    SwipeDismissLayout(
        state = state,
        headerContent = {
            IconButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    close()

                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.White,
                        contentDescription = "Close"
                    )
                }
            )
        },
        content = { swipePercent ->
            AddOverlayContent(swipePercent)
        }
    )
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
@ExperimentalFoundationApi
//private fun AddOverlayContent() {
private fun AddOverlayContent(swipePercent: Float) {
    val columnPadding = 16.dp

    var titleInputValue by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = columnPadding)
            .heightIn(min = 200.dp),
        backgroundColor = Color.White,
        content = {
            Column(Modifier.padding(16.dp)) {
                Text(swipePercent.toString(), fontSize = 12.sp)
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

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(columnPadding)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = columnPadding)
            .heightIn(min = 100.dp),
        backgroundColor = Color.White,
        content = {
            Text("Other", color = Color.Black, fontSize = 24.sp)
        }
    )


    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    )
}

@ExperimentalFoundationApi
@Composable
fun FlatTextField(
    text: String,
    modifier: Modifier = Modifier,
    placeholderText: String? = null,
    textStyle: TextStyle = TextStyle.Default,
    onTextChange: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val activeIndicatorColor = Color.Red
    val inactiveIndicatorColor = Color.Gray
    val animatedIndicatorColor = Animatable(inactiveIndicatorColor)

    fun updateState(active: Boolean) {
        coroutineScope.launch {
            val targetIndicatorColor = if (active) activeIndicatorColor else inactiveIndicatorColor
            animatedIndicatorColor.animateTo(targetIndicatorColor, animationSpec = tween(durationMillis = 200, easing = LinearEasing))
        }
    }

    @Px var textFieldHeightPx by remember { mutableStateOf(0) }
    val textFieldHeight = with(LocalDensity.current) { textFieldHeightPx.toDp() }


    Box(Modifier.onSizeChanged { textFieldHeightPx = it.height }) {
        if (placeholderText != null && text.isEmpty()) {
            Text(placeholderText, style = textStyle, modifier = modifier)
        }

        BasicTextField(
            value = text,
            singleLine = true,
            onValueChange = onTextChange,
            textStyle = textStyle,
            modifier = modifier.onFocusChanged {
                updateState(it == FocusState.Active)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions {
                //TODO: On action
            }
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


@ExperimentalFoundationApi
@Composable
@Preview
fun previewFlatTextField() {
    val textValue = "Something"

    FlavourJournalTheme {
        FlatTextField(
            text = textValue,
            onTextChange = {}
        )
    }
}

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