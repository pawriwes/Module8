package com.parivesh.cookingtime.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getAllFavorites(): LiveData<List<Recipe>>

    @Query("DELETE FROM recipes")
    suspend fun deleteAllRecipes()

    @Query("SELECT * FROM recipes WHERE idMeal = :idMeal")
    suspend fun getRecipeById(idMeal: String): Recipe?
}
