package com.example.simplenotepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplenotepad.arch.BaseApplication
import com.example.simplenotepad.databinding.FragmentAddCategoryBinding
import com.example.simplenotepad.home.HomeCategoriesEpoxyController
import com.example.simplenotepad.room.CategoryEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class AddCategoryFragment : BaseApplication() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    private val addCategoriesEpoxyController = AddCategoryEpoxyController(::clickState)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.transactionCompleteListener.observe(viewLifecycleOwner){
            it.getContent()?.let {
                binding.addCategoryEditText.setText("")
            }
        }

        sharedViewModel.categoryEntitiesLiveData.observe(viewLifecycleOwner){
            addCategoriesEpoxyController.categoryEntities = it
        }

        binding.addCategoryEpoxyRecyclerView.setController(addCategoriesEpoxyController)

        binding.saveCategoryButton.setOnClickListener {
            saveToDatabase()
        }
    }

    private fun saveToDatabase() {
        val categoryName = binding.addCategoryEditText.text.toString().trim()

        if(categoryName.isBlank()){
            binding.addCategoryTextField.error = "*Cannot be blank"
            return
        }

        binding.addCategoryTextField.error = null

        val categoryEntity = CategoryEntity(
            id = UUID.randomUUID().toString(),
            categoryName = categoryName.lowercase()
        )

        sharedViewModel.addCategory(categoryEntity)

    }

    private fun clickState(category : CategoryEntity){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete")
            .setMessage("Are you sure to remove \"${category.categoryName.replaceFirstChar { it.uppercase() }}\" category?")
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Delete") { dialog, which ->
                sharedViewModel.deleteCategory(category)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}