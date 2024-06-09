package com.parivesh.cookingtime.ui.favorites

import android.app.Application
import androidx.lifecycle.*
import com.parivesh.cookingtime.model.Recipe
import com.parivesh.cookingtime.model.RecipeDatabase
import com.parivesh.cookingtime.repository.FavoritesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoritesRepository
    val favoriteRecipes: LiveData<List<Recipe>>

    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = FavoritesRepository(recipeDao)
        favoriteRecipes = repository.favoriteRecipes
    }

    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }
}
