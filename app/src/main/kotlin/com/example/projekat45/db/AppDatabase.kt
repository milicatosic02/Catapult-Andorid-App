package com.example.projekat45.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projekat45.cats.db.BreedsDao
import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.cats.db.images.BreedsGallery
import com.example.projekat45.cats.db.images.BreedsGalleryDao
//val MIGRATION_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        // Dodavanje nove kolone temperament
//        database.execSQL("ALTER TABLE BreedsData ADD COLUMN temperament TEXT")
//    }
//}

@Database(
    entities = [
        BreedsData::class,
        BreedsGallery::class,
    ],
    version = 1,
    exportSchema = true,
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breedsDao():BreedsDao
    abstract fun breedsGalleryDao(): BreedsGalleryDao
}