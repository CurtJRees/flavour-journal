package com.curtjrees.flavours.db

import com.curtjrees.flavours.FlavourEntryDb

object DbFlavourMapper {

    fun map(item: FlavourEntryDb): DbFlavourEntry = with(item) {
        DbFlavourEntry(
            id = id,
            name = name
        )
    }

}