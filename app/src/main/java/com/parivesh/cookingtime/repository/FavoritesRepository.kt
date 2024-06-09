package com.parivesh.cookingtime.repository

import androidx.lifecycle.LiveData
import com.parivesh.cookingtime.model.Recipe
import com.parivesh.cookingtime.model.RecipeDao

class FavoritesRepository(private val recipeDao: RecipeDao) {

    val favoriteRecipes: LiveData<List<Recipe>> = recipeDao.getAllFavorites()

    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    suspend fun deleteAllRecipes() {
        recipeDao.deleteAllRecipes()
    }

    suspend fun getRecipeById(idMeal: String): Recipe? {
        return recipeDao.getRecipeById(idMeal)
    }
}
