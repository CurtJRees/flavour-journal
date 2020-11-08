package com.curtjrees.flavours

import kotlinx.coroutines.flow.Flow

interface FlavoursDataSource {

    fun getAll(): Flow<List<FlavourEntry>>

    fun add(name: String)

}

interface FlavourEntry {
    val id: Long
    val name: String
}