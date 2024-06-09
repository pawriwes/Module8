package com.parivesh.cookingtime.ui.favorites

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

class FavoritesAdapter(
    private var recipes: MutableList<Recipe>,
    private val onUnfavoriteClick: (Recipe) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.RecipeViewHolder>() {

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

            btnFavorite.setImageResource(R.drawable.baseline_favorite_24)

            btnFavorite.setOnClickListener {
                onUnfavoriteClick(recipe)
                recipes.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("recipeId", recipe.idMeal)
                it.findNavController().navigate(R.id.action_favoritesFragment_to_recipeDetailFragment, bundle)
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
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }
}
