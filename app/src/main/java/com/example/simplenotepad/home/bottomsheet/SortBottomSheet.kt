package com.example.simplenotepad.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.simplenotepad.MainActivity
import com.example.simplenotepad.R
import com.example.simplenotepad.add.CategoriesEpoxyController
import com.example.simplenotepad.arch.NoteViewModel
import com.example.simplenotepad.databinding.FragmentSortBottomSheetBinding
import com.example.simplenotepad.room.CategoryEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SortBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentSortBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSortBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val epoxyController = BottomSheetEpoxyController(
            viewModel.currentSort,
            NoteViewModel.HomeViewState.Sort.values()
        ) { selectedSort ->
            viewModel.currentSort = selectedSort
            dismiss()
        }

        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

        val categoriesEpoxyController = CategoriesEpoxyController(::onCategorySelected, ::onEmptyState)

        viewModel.onCategorySelected(viewModel.sortByCategoryId, true)
        viewModel.categoriesViewStateLiveData.observe(viewLifecycleOwner){
            categoriesEpoxyController.categoryEntities = it
        }

        binding.categoryEpoxyRecyclerView.setControllerAndBuildModels(categoriesEpoxyController)
    }

    private fun onEmptyState() {
        binding.categoryEpoxyRecyclerView.isGone = true
        binding.sortLabelTextView2.isGone = true
    }

    private fun onCategorySelected(categoryId: String) {
        viewModel.sortByCategoryId = categoryId
        viewModel.onCategorySelected(categoryId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}