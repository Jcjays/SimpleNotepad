package com.example.simplenotepad.home.bottomsheet

import android.graphics.Typeface
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.R
import com.example.simplenotepad.arch.NoteViewModel
import com.example.simplenotepad.databinding.ModelSortOrderItemBinding
import com.example.simplenotepad.model.ViewBindingKotlinModel

class BottomSheetEpoxyController(
    private val selectedSort: NoteViewModel.HomeViewState.Sort,
    private val sortOptions: Array<NoteViewModel.HomeViewState.Sort>,
    private val selectedCallback: (NoteViewModel.HomeViewState.Sort) -> Unit
) : EpoxyController() {

    override fun buildModels() {
        sortOptions.forEach {
            val isSelected = it.name == selectedSort.name
           SortOrderItemEpoxyModel(isSelected, it, selectedCallback).id(it.name).addTo(this)
        }
    }

    data class SortOrderItemEpoxyModel(
        val isSelected: Boolean,
        val sort: NoteViewModel.HomeViewState.Sort,
        val selectedCallback: (NoteViewModel.HomeViewState.Sort) -> Unit
    ): ViewBindingKotlinModel<ModelSortOrderItemBinding>(R.layout.model_sort_order_item) {

        override fun ModelSortOrderItemBinding.bind() {
            sortName.text = sort.name
            root.setOnClickListener { selectedCallback(sort) }

            // Styling for selected option
            sortName.typeface = if (isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            view.visibility = if (isSelected) View.VISIBLE else View.GONE
        }
    }

}