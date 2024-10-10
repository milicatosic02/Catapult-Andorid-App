package com.example.projekat45.cats.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedsDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedsData>)

    @Query("SELECT * FROM BreedsData")
    fun getAll(): Flow<List<BreedsData>>

    @Query("SELECT * FROM BreedsData WHERE id =:breedId")
    fun getBreedById(breedId: String): Flow<BreedsData>


}