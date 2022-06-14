package com.example.simplenotepad.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.R
import com.example.simplenotepad.arch.NoteViewModel
import com.example.simplenotepad.databinding.ModelHeaderTitleBinding
import com.example.simplenotepad.databinding.ModelNoteDisplayBinding
import com.example.simplenotepad.model.EmptyStateModel
import com.example.simplenotepad.model.LoadingStateModel
import com.example.simplenotepad.model.ViewBindingKotlinModel
import com.example.simplenotepad.room.NoteWithCategories


class HomeEpoxyController(
    private val clickState : IClickableState
) : EpoxyController() {

    var isSelectionModeEnabled : Boolean = false
        set(value) {
            field = value
            requestModelBuild()
        }

    var entities : NoteViewModel.HomeViewState = NoteViewModel.HomeViewState(isLoading = true)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if(entities.isLoading){
            LoadingStateModel().id("loading").addTo(this)
            return
        }

        if(entities.dataList.size < 2){
            EmptyStateModel().id("empty").addTo(this)
            return
        }

        entities.dataList.forEach { dataItem ->

            if(dataItem.isHeader){
                HeaderEpoxyModel(dataItem.data as String).id("header").addTo(this)
                return@forEach
            }

            val itemWithCategoryEntity = dataItem.data as NoteWithCategories

            NoteModelDisplay(itemWithCategoryEntity, clickState, isSelectionModeEnabled)
                .id(itemWithCategoryEntity.noteEntity.noteId)
                .addTo(this)
        }

    }
}


data class NoteModelDisplay(
    val note: NoteWithCategories,
    val clickState: IClickableState,
    val isSelectionModeEnable: Boolean
): ViewBindingKotlinModel<ModelNoteDisplayBinding>(R.layout.model_note_display){
    override fun ModelNoteDisplayBinding.bind() {
        title.text = note.noteEntity.title

        if(note.noteEntity.content == null)
            content.isGone = true
        else{
            content.isVisible = true
            content.text = note.noteEntity.content
        }
        date.text = note.noteEntity.dateCreated

        if(note.noteEntity.color.isNotBlank())
            root.setCardBackgroundColor(Color.parseColor(note.noteEntity.color))

        if(isSelectionModeEnable){
            radioButton.visibility = View.VISIBLE
            root.setOnClickListener {
                clickState.onMultipleSelectionEnabled(note.noteEntity.noteId)
                radioButton.isChecked = !radioButton.isChecked
            }
        } else {
            radioButton.visibility = View.GONE
            radioButton.isChecked = false
            root.setOnClickListener {
                clickState.onSelectedItem(note.noteEntity.noteId)
            }
        }

        root.setOnLongClickListener {
            clickState.onMultipleSelectionEnabled(note.noteEntity.noteId)
            radioButton.isChecked = !radioButton.isChecked
            true
        }
    }
}

data class HeaderEpoxyModel(
    val headerText: String
): ViewBindingKotlinModel<ModelHeaderTitleBinding>(R.layout.model_header_title) {

    @SuppressLint("SetTextI18n")
    override fun ModelHeaderTitleBinding.bind() {
        headerTitle.text = "Sorted by: ${headerText.lowercase().replaceFirstChar { it.uppercase() }}"
    }
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}