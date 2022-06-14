package com.example.simplenotepad.add

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.R
import com.example.simplenotepad.arch.NoteViewModel
import com.example.simplenotepad.databinding.ModelCategoryButtonsBinding
import com.example.simplenotepad.model.ViewBindingKotlinModel


class CategoriesEpoxyController(
    private val onCategorySelected : (String) -> Unit,
    private val onEmptyState: () -> Unit
) : EpoxyController() {

    var categoryEntities = NoteViewModel.CategoriesViewState()
        set(value) {
        field = value
        requestModelBuild()
    }

    override fun buildModels() {
//        if(categoryEntities.itemList.isEmpty()){
//            onEmptyState()
//            return
//        }

        categoryEntities.itemList.forEach { category ->
            CategoryModels(category, onCategorySelected).id(category.categoryEntity.id).addTo(this)
        }
    }

    data class CategoryModels(
        val category: NoteViewModel.CategoriesViewState.CategoryItem,
        val onCategorySelected: (String) -> Unit
    ): ViewBindingKotlinModel<ModelCategoryButtonsBinding>(R.layout.model_category_buttons) {
        override fun ModelCategoryButtonsBinding.bind() {
            categoryName.text = category.categoryEntity.categoryName

            if(category.isSelected) root.strokeColor = Color.parseColor("#FF9800")
            else root.strokeColor = Color.parseColor("#60000000")

            root.setOnClickListener {
                onCategorySelected(category.categoryEntity.id)
            }
        }
    }

}