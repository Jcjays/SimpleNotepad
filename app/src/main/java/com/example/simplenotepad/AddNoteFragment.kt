package com.example.simplenotepad

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.simplenotepad.arch.BaseApplication
import com.example.simplenotepad.databinding.FragmentAddNoteBinding
import com.example.simplenotepad.databinding.FragmentHomeBinding
import com.example.simplenotepad.room.NoteEntity
import java.text.DateFormat
import java.util.*


class AddNoteFragment : BaseApplication() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private var isInEditMode : Boolean = false
    private val noteId : AddNoteFragmentArgs by navArgs()
    private val existingNote : NoteEntity? by lazy {
        sharedViewModel.noteEntities.value?.find {
            it.noteId == noteId.noteIdAction
        }
    }

    private val colorWheelEpoxyController = ColorWheelEpoxyController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyboard(binding.titleEditText)

        sharedViewModel.transactionCompleteListener.observe(viewLifecycleOwner){
            it.getContent()?.let {
                resetTextFieldState()
            }
        }

        binding.colorWheel.setControllerAndBuildModels(colorWheelEpoxyController)

        //populate text view in edit mode
        existingNote.let {
            if(it == null)
                return@let

            isInEditMode = true
            populateExistingData(it)
        }

        binding.saveButton.setOnClickListener {
            saveToDatabase()
        }
    }

    private fun saveToDatabase(){
        val title = binding.titleEditText.text.toString().trim()
        var content : String? = binding.contentEditText.text.toString().trim()

        if(title.isEmpty()) {
            binding.titleTextField.error = "* Required"
            return
        }

        binding.titleTextField.error = null

        if(content?.isEmpty() == true)
            content = null

        if(isInEditMode){
            val noteEntity = existingNote!!.copy(
                title = title,
                content = content,
                dateCreated = DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)
            )

            sharedViewModel.updateNote(noteEntity)
            Toast.makeText(requireContext(), "Item updated!", Toast.LENGTH_SHORT).show()
            return
        }

        //todo make a state when in edit mode.

        val noteEntity = NoteEntity(
            noteId = UUID.randomUUID().toString(),
            title = title,
            content = content,
            dateCreated = DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)
        )

        sharedViewModel.addNote(noteEntity)
    }

    private fun resetTextFieldState() {
        binding.titleEditText.text = null
        binding.contentEditText.text = null
        binding.titleEditText.requestFocus()
        Toast.makeText(requireContext(), "Item saved!", Toast.LENGTH_SHORT).show()
    }

    private fun populateExistingData(note : NoteEntity?){
        binding.titleEditText.setText(note?.title)
        binding.contentEditText.setText(note?.content)
        binding.saveButton.text = "Update"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}