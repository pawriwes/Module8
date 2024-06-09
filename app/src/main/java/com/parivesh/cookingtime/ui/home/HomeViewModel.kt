package com.parivesh.cookingtime.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.parivesh.cookingtime.model.Recipe
import com.parivesh.cookingtime.model.RecipeDatabase
import com.parivesh.cookingtime.repository.FavoritesRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val repository: FavoritesRepository
    private val requestQueue: RequestQueue

    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = FavoritesRepository(recipeDao)
        requestQueue = Volley.newRequestQueue(application)
        fetchRecipes() // Fetch recipes initially when the ViewModel is created
    }

    fun fetchRecipes() {
        if (_recipes.value.isNullOrEmpty()) {
            val url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=Seafood"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                { response ->
                    val recipesList = parseRecipes(response)
                    viewModelScope.launch {
                        // Observe favorite recipes from Room database
                        repository.favoriteRecipes.observeForever { favoriteRecipes ->
                            val updatedRecipesList = recipesList.map { recipe ->
                                recipe.isFavorite = favoriteRecipes.any { it.idMeal == recipe.idMeal }
                                recipe
                            }
                            _recipes.value = updatedRecipesList
                        }
                    }
                },
                { error ->
                    // Handle error
                }
            )
            requestQueue.add(jsonObjectRequest)
        }
    }

    private fun parseRecipes(response: JSONObject): List<Recipe> {
        val mealsArray = response.getJSONArray("meals")
        val gson = Gson()
        val recipes = mutableListOf<Recipe>()
        for (i in 0 until mealsArray.length()) {
            val mealObject = mealsArray.getJSONObject(i)
            val recipe = gson.fromJson(mealObject.toString(), Recipe::class.java)
            recipes.add(recipe)
        }
        return recipes
    }

    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }

    fun delete(recipe: Recipe) = viewModelScope.launch {
        repository.delete(recipe)
    }
}
