package com.example.simplenotepad.model

import android.view.View
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
            NoteModelDisplay(note, clickState)
                .id(note.noteId)
                .addTo(this)
        }

    }
}

data class NoteModelDisplay(
    val note: NoteEntity,
    val clickState: IClickableState
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

        root.setOnClickListener {
            clickState.onSelectedItem(note.noteId)
        }
    }

}