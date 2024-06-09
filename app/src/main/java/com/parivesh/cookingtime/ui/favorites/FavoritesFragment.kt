package com.parivesh.cookingtime.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parivesh.cookingtime.R
import com.parivesh.cookingtime.model.Recipe

class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: FavoritesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvNoFavorites: TextView
    private val favoriteRecipes = mutableListOf<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewFavorites)
        tvNoFavorites = view.findViewById(R.id.tvNoFavorites)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoritesAdapter(favoriteRecipes) { recipe ->
            viewModel.delete(recipe)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        viewModel.favoriteRecipes.observe(viewLifecycleOwner, Observer { recipes ->
            favoriteRecipes.clear()
            favoriteRecipes.addAll(recipes)
            adapter.notifyDataSetChanged()

            if (recipes.isEmpty()) {
                recyclerView.visibility = View.GONE
                tvNoFavorites.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                tvNoFavorites.visibility = View.GONE
            }
        })
    }
}
