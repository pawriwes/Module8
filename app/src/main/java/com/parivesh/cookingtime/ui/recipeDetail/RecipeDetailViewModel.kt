package com.parivesh.cookingtime.ui.recipedetail

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

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipe = MutableLiveData<Recipe?>()
    val recipe: MutableLiveData<Recipe?> = _recipe

    private val repository: FavoritesRepository
    private val requestQueue: RequestQueue

    init {
        val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
        repository = FavoritesRepository(recipeDao)
        requestQueue = Volley.newRequestQueue(application)
    }

    fun fetchRecipeDetail(recipeId: String) {
        viewModelScope.launch {
            val localRecipe = repository.getRecipeById(recipeId)
            if (localRecipe != null && localRecipe.storeLocally) {
                _recipe.value = localRecipe
            } else {
                fetchRecipeFromApi(recipeId)
            }
        }
    }

    private fun fetchRecipeFromApi(recipeId: String) {
        val url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$recipeId"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val recipe = parseRecipe(response)
                recipe.storeLocally = true
                viewModelScope.launch {
                    repository.insert(recipe)
                }
                _recipe.value = recipe
            },
            { error ->
                // Handle error
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun parseRecipe(response: JSONObject): Recipe {
        val mealsArray = response.getJSONArray("meals")
        val mealObject = mealsArray.getJSONObject(0)
        val gson = Gson()
        return gson.fromJson(mealObject.toString(), Recipe::class.java)
    }
}
