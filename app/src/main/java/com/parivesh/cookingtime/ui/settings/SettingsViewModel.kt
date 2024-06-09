package com.parivesh.cookingtime.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.parivesh.cookingtime.model.RecipeDatabase
import com.parivesh.cookingtime.repository.FavoritesRepository
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoritesRepository

    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = FavoritesRepository(recipeDao)
    }

    fun deleteAllRecipes() = viewModelScope.launch {
        repository.deleteAllRecipes()
    }
}
