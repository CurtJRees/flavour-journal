package com.curtjrees.flavours.models

import com.curtjrees.flavours.FlavourEntry

data class FlavourUiItem(
    val id: Long,
    val name: String
)

object FlavourUiItemMapper {

    fun map(item: FlavourEntry): FlavourUiItem = with(item) {
        FlavourUiItem(
            id = id,
            name = name
        )
    }

}