package com.example.simplenotepad.add

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.navigation.fragment.navArgs
import com.example.simplenotepad.arch.BaseApplication
import com.example.simplenotepad.databinding.FragmentAddNoteBinding
import com.example.simplenotepad.room.CategoryEntity
import com.example.simplenotepad.room.NoteEntity
import java.text.DateFormat
import java.util.*


class AddNoteFragment : BaseApplication() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private var isInEditMode : Boolean = false

    private val noteId : AddNoteFragmentArgs by navArgs()
    private val existingNote : NoteEntity? by lazy {
        sharedViewModel.notesWithCategoriesLiveData.value?.find {
            it.noteEntity.noteId == noteId.noteIdAction
        }?.noteEntity
    }

    private var color: String = Colors.DefaultColor.color
    private val colorWheelEpoxyController = ColorWheelEpoxyController{ colorAttribute ->
        binding.root.setBackgroundColor(Color.parseColor(colorAttribute))
        color = colorAttribute
    }


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

        //region populate text view in edit mode
        existingNote.let {
            if(it == null)
                return@let

            isInEditMode = true
            populateExistingData(it)
        }

        binding.saveButton.setOnClickListener {
            saveToDatabase()
        }
        // endregion
        val categoriesEpoxyController = CategoriesEpoxyController (::onCategorySelected, ::onEmptyState)

        sharedViewModel.onCategorySelected(existingNote?.categoryId ?: CategoryEntity.DEFAULT_CATEGORY_ID, true)
        sharedViewModel.categoriesViewStateLiveData.observe(viewLifecycleOwner){
            categoriesEpoxyController.categoryEntities = it
        }

        binding.addNoteCategoriesEpoxyRecyclerView.setController(categoriesEpoxyController)
    }

    private fun onEmptyState() {
        binding.label2.isGone = true
        binding.addNoteCategoriesEpoxyRecyclerView.isGone = true
    }

    private fun onCategorySelected(categoryId: String) {
        sharedViewModel.onCategorySelected(categoryId)
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

        val itemCategoryId = sharedViewModel.categoriesViewStateLiveData.value?.getSelectedCategoryId() ?: return

        if(isInEditMode){
            val noteEntity = existingNote!!.copy(
                title = title,
                content = content,
                dateCreated = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Calendar.getInstance().time),
                color = color,
                categoryId = itemCategoryId
            )

            sharedViewModel.updateNote(noteEntity)
            Toast.makeText(requireContext(), "Item updated!", Toast.LENGTH_SHORT).show()
            return
        }

        val noteEntity = NoteEntity(
            noteId = UUID.randomUUID().toString(),
            title = title,
            content = content,
            dateCreated = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Calendar.getInstance().time),
            color = color,
            categoryId = itemCategoryId
        )

        sharedViewModel.addNote(noteEntity)
    }

    private fun resetTextFieldState() {
        binding.titleEditText.text = null
        binding.contentEditText.text = null
        binding.titleEditText.requestFocus()
        sharedViewModel.onCategorySelected(CategoryEntity.DEFAULT_CATEGORY_ID)
        Toast.makeText(requireContext(), "Item saved!", Toast.LENGTH_SHORT).show()
    }

    private fun populateExistingData(note : NoteEntity?){
        binding.titleEditText.setText(note?.title)
        binding.contentEditText.setText(note?.content)
        binding.saveButton.text = "Update"

        if(existingNote?.color!!.isNotBlank()){
            binding.root.setBackgroundColor(Color.parseColor(existingNote?.color))
            color = existingNote?.color ?: Colors.DefaultColor.color
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}