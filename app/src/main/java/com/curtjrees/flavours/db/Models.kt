package com.curtjrees.flavours.db

import com.curtjrees.flavours.FlavourEntry

data class DbFlavourEntry(
    override val id: Long,
    override val name: String
): FlavourEntry