package com.example.simplenotepad.model

import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.example.simplenotepad.IClickableState
import com.example.simplenotepad.R
import com.example.simplenotepad.databinding.ModelNoteDisplayBinding
import com.example.simplenotepad.room.NoteEntity

class HomeEpoxyController(
    val clickState : IClickableState
) : EpoxyController() {


    private var isLoading : Boolean = false
        set(value) {
            field = value
            requestModelBuild()
        }

    var isSelectionModeEnabled : Boolean = false
        set(value) {
            field = value
            requestModelBuild()
        }

    var noteEntity = ArrayList<NoteEntity>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        if(isLoading){
            LoadingStateModel().id("loading").addTo(this)
        }

        noteEntity.forEach { note ->
            NoteModelDisplay(note, clickState, isSelectionModeEnabled)
                .id(note.noteId)
                .addTo(this)
        }

    }
}

data class NoteModelDisplay(
    val note: NoteEntity,
    val clickState: IClickableState,
    val isSelectionModeEnable: Boolean
): ViewBindingKotlinModel<ModelNoteDisplayBinding>(R.layout.model_note_display){
    override fun ModelNoteDisplayBinding.bind() {

        title.text = note.title

        if(note.content == null)
            content.isGone = true
        else{
            content.isVisible = true
            content.text = note.content
        }
        date.text = note.dateCreated

        if(note.color.isNotBlank())
            root.setCardBackgroundColor(Color.parseColor(note.color))

        if(isSelectionModeEnable){
            radioButton.visibility = View.VISIBLE
            root.setOnClickListener {
                clickState.onMultipleSelectionEnabled(note.noteId)
                radioButton.isChecked = !radioButton.isChecked
            }
        } else {
            radioButton.visibility = View.GONE
            radioButton.isChecked = false
            root.setOnClickListener {
                clickState.onSelectedItem(note.noteId)
            }
        }

        root.setOnLongClickListener {
            clickState.onMultipleSelectionEnabled(note.noteId)
            radioButton.isChecked = !radioButton.isChecked
            true
        }



    }

}