package com.example.projekat45.cats.repository

import com.example.projekat45.cats.api.BreedsApi
import com.example.projekat45.cats.api.ResultsApi
import com.example.projekat45.cats.api.model.ResultModel
import com.example.projekat45.cats.db.BreedsDao
import com.example.projekat45.cats.db.BreedsData
import com.example.projekat45.cats.db.images.BreedsGallery
import com.example.projekat45.cats.db.images.BreedsGalleryDao
import com.example.projekat45.db.AppDatabase
import com.example.projekat45.users.UsersData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedsRepository @Inject constructor(
    private val breedsDao: BreedsDao,
    private val breedsGalleryDao: BreedsGalleryDao,
    private val breedsApi: BreedsApi,
    private val resultsApi: ResultsApi
){
    suspend fun fetchAllBreeds(){
        breedsDao.insertAll(breeds = breedsApi.getAllBreeds())
    }

    fun getAllBreedsFlowFromDb(): Flow<List<BreedsData>> = breedsDao.getAll()

    fun getBreedsByIdFlow(id: String): Flow<BreedsData> = breedsDao.getBreedById(id)

    suspend fun getAllBreedsPhotosApi(id: String): List<BreedsGallery> {
        val images = breedsApi.getAllBreedsPhotos(id).map { it.copy(id = id) }
        breedsGalleryDao.insertAllGalleryBreeds(breeds = images)
        return images
    }

    fun getAllBreedsPhotoByIdFLow(id: String): Flow<List<String>> = breedsGalleryDao.getAllImagesForId(id)

    suspend fun postResult(nickname: String, result:Float, category: Int) {
        val model = ResultModel(nickname,result,category)
        resultsApi.postResult(model)
    }
    suspend fun fetchAllResultsForCategory(category: Int): List<ResultModel> {
        return resultsApi.getAllResultsForCategory(category)
    }
}
