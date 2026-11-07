
package com.example.miapp_grupox.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        LineBebedero::class,
        FlusingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bebederoDao(): BebederoDao
    abstract fun flusingDao(): FlusingDao
}