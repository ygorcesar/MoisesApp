package com.example.moiseschallenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moiseschallenge.data.local.dao.TrackDao
import com.example.moiseschallenge.data.local.entity.CachedTrackEntity

@Database(
    entities = [CachedTrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoisesDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

    companion object {
        const val DATABASE_NAME = "moises_database"
    }
}
