package com.curtjrees.flavours

import android.content.Context
import com.curtjrees.flavours.db.DbFlavourMapper
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlavoursRepository(context: Context): FlavoursDataSource {

    private val dbDriver = AndroidSqliteDriver(Database.Schema, context, DB_NAME)
    private val database = Database(dbDriver)

    override fun getAll(): Flow<List<FlavourEntry>> {
        return database.flavoursQueries.getAll().asFlow().mapToList().map {
            it.map { dbItem ->
                DbFlavourMapper.map(dbItem)
            }
        }
    }

    override fun add(name: String) = database.flavoursQueries.insert(name)

    private companion object {
        const val DB_NAME = "flavours.db"
    }
}