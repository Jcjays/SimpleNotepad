package com.example.simplenotepad

import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.arch.NoteViewModel
import com.example.simplenotepad.databinding.ModelCategoryButtonsBinding
import com.example.simplenotepad.model.ViewBindingKotlinModel
import com.example.simplenotepad.room.CategoryEntity

class AddCategoryEpoxyController(
    private val clickState: (CategoryEntity) -> Unit
) : EpoxyController() {

    var categoryEntities: List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        categoryEntities.forEach { categoryEntity ->
            CategoryModels(categoryEntity, clickState).id(categoryEntity.id).addTo(this)
        }
    }

    data class CategoryModels(
        val category: CategoryEntity,
        val clickState: (CategoryEntity) -> Unit
    ) : ViewBindingKotlinModel<ModelCategoryButtonsBinding>(R.layout.model_category_buttons) {
        override fun ModelCategoryButtonsBinding.bind() {
            categoryName.text = category.categoryName

            root.setOnLongClickListener {
                clickState(category)
                true
            }
        }
    }

}