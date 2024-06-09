package com.parivesh.cookingtime.ui.recipedetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.parivesh.cookingtime.R

class RecipeDetailFragment : Fragment() {

    private lateinit var viewModel: RecipeDetailViewModel
    private lateinit var imgRecipeThumb: ImageView
    private lateinit var tvRecipeTitle: TextView
    private lateinit var tvRecipeCategory: TextView
    private lateinit var tvRecipeInstructions: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        imgRecipeThumb = view.findViewById(R.id.imgRecipeThumb)
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle)
        tvRecipeCategory = view.findViewById(R.id.tvRecipeCategory)
        tvRecipeInstructions = view.findViewById(R.id.tvRecipeInstructions)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RecipeDetailViewModel::class.java)

        val recipeId = arguments?.getString("recipeId")
        Log.d("RecipeDetailFragment", "Received recipeId: $recipeId")

        if (recipeId != null) {
            viewModel.fetchRecipeDetail(recipeId)
        } else {
            Log.e("RecipeDetailFragment", "recipeId is null")
        }

        viewModel.recipe.observe(viewLifecycleOwner, Observer { recipe ->
            recipe?.let {
                Log.d("RecipeDetailFragment", "Displaying recipe: $recipe")
                Glide.with(this)
                    .load(recipe.strMealThumb)
                    .into(imgRecipeThumb)
                tvRecipeTitle.text = recipe.strMeal
                tvRecipeCategory.text = recipe.strCategory
                tvRecipeInstructions.text = recipe.strInstructions
            } ?: run {
                Log.e("RecipeDetailFragment", "Recipe is null")
            }
        })
    }
}
