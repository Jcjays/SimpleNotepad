package com.example.simplenotepad.home

import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.R
import com.example.simplenotepad.databinding.ModelCategoryButtonsBinding
import com.example.simplenotepad.model.ViewBindingKotlinModel
import com.example.simplenotepad.room.CategoryEntity

class HomeCategoriesEpoxyController(
    private val clickState : IClickableState
) : EpoxyController() {

    var categoryEntities : List<CategoryEntity> = emptyList()
        set(value) {
        field = value
        requestModelBuild()
    }

    override fun buildModels() {
        clickState.isEmpty(categoryEntities.isEmpty())

        categoryEntities.forEach { categoryEntity ->
            CategoryModels(categoryEntity).id(categoryEntity.id).addTo(this)
        }
    }

    data class CategoryModels(
        val category: CategoryEntity
    ): ViewBindingKotlinModel<ModelCategoryButtonsBinding>(R.layout.model_category_buttons) {
        override fun ModelCategoryButtonsBinding.bind() {
            itemCategory.text = category.categoryName
        }
    }

}