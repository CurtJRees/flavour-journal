package com.curtjrees.flavours.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.curtjrees.flavours.FlavoursRepository
import com.curtjrees.flavours.models.FlavourUiItem
import com.curtjrees.flavours.models.FlavourUiItemMapper
import com.curtjrees.flavours.navigation.Screen

@Composable
fun HomeScreen(
    navigationEventCallback: (Screen) -> Unit,
    showAddOverlayCallback: () -> Unit
) {
    //TODO: Move logic into ViewModel
    val flavoursRepository = FlavoursRepository(LocalContext.current)
    val flavours = flavoursRepository.getAll().collectAsState(initial = emptyList())

    val uiItems = flavours.value.map {
        FlavourUiItemMapper.map(it)
    }

    HomeContent(uiItems, onAddClick = showAddOverlayCallback)
}

@Composable
private fun HomeContent(
    items: List<FlavourUiItem>,
    onAddClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(Modifier.fillMaxSize()) {
            items(items) {
                Text("${it.id} - ${it.name}")
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onAddClick,
            content = {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun previewHomeContent() {
    HomeContent(
        items = listOf(
            FlavourUiItem(0, "First"),
            FlavourUiItem(1, "Second"),
        )
    )
}