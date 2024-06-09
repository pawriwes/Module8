package com.parivesh.cookingtime.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parivesh.cookingtime.R
import com.parivesh.cookingtime.model.Recipe

class HomeAdapter(
    private var recipes: List<Recipe>,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<HomeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgThumbnail: ImageView = itemView.findViewById(R.id.imgThumbnail)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)

        fun bind(recipe: Recipe) {
            Glide.with(imgThumbnail.context)
                .load(recipe.strMealThumb)
                .placeholder(R.drawable.baseline_3p_24)
                .into(imgThumbnail)

            tvTitle.text = recipe.strMeal

            btnFavorite.setImageResource(
                if (recipe.isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border
            )

            btnFavorite.setOnClickListener {
                recipe.isFavorite = !recipe.isFavorite
                if (recipe.isFavorite) {
                    viewModel.insert(recipe)
                } else {
                    viewModel.delete(recipe)
                }
                notifyItemChanged(adapterPosition)
            }

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("recipeId", recipe.idMeal)
                it.findNavController().navigate(R.id.action_homeFragment_to_recipeDetailFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_favorite_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount() = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
