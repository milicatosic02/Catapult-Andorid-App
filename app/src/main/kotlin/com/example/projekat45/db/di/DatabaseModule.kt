package com.example.projekat45.db.di

import com.example.projekat45.cats.db.BreedsDao
import com.example.projekat45.cats.db.images.BreedsGalleryDao
import com.example.projekat45.db.AppDatabase
import com.example.projekat45.db.AppDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(builder: AppDatabaseBuilder):AppDatabase{
        return builder.build()
    }

    @Provides
    fun provideBreedsDao(database: AppDatabase): BreedsDao {
        return database.breedsDao()
    }
    @Provides
    fun provideBreedsGalleryDao(database: AppDatabase): BreedsGalleryDao = database.breedsGalleryDao()
}