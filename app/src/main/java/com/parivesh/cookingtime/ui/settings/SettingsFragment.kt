package com.parivesh.cookingtime.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.parivesh.cookingtime.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val btnClearStorage = binding.btnClearStorage

        btnClearStorage.setOnClickListener {
            showConfirmationDialog()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm")
            .setMessage("Do you really want to clear all local storage?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, _ ->
                clearLocalStorage()
                dialog.dismiss()
            }
            .show()
    }

    private fun clearLocalStorage() {
        settingsViewModel.deleteAllRecipes()
        showSuccessDialog()
    }

    private fun showSuccessDialog() {
        Toast.makeText(this.context, "Local Storage Cleared", Toast.LENGTH_SHORT).show()
    }
}