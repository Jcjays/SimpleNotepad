package com.example.simplenotepad

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.simplenotepad.arch.BaseApplication
import com.example.simplenotepad.databinding.FragmentAddNoteBinding
import com.example.simplenotepad.databinding.FragmentHomeBinding
import com.example.simplenotepad.room.NoteEntity
import java.text.DateFormat
import java.util.*


class AddNoteFragment : BaseApplication() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showSoftKeyboard(binding.titleEditText)

        sharedViewModel.transactionCompleteListener.observe(viewLifecycleOwner){
            it.getContent()?.let {
                resetTextFieldState()
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}