package com.example.simplenotepad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.example.simplenotepad.arch.BaseApplication
import com.example.simplenotepad.databinding.FragmentHomeBinding
import com.example.simplenotepad.databinding.ModelNoteDisplayBinding
import com.example.simplenotepad.model.HomeEpoxyController
import com.example.simplenotepad.model.NoteModelDisplay
import com.example.simplenotepad.room.NoteEntity


class HomeFragment : BaseApplication(), IClickableState {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeEpoxyController = HomeEpoxyController(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.HomeFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        sharedViewModel.noteEntities.observe(viewLifecycleOwner){
            homeEpoxyController.noteEntity = it as ArrayList<NoteEntity>
        }

        binding.HomeEpoxyRecyclerView.setController(homeEpoxyController)

        EpoxyTouchHelper.initSwiping(binding.HomeEpoxyRecyclerView)
            .left()
            .withTarget(NoteModelDisplay::class.java) // Specify the type of model or models that should be swipable
            .andCallbacks(object : SwipeCallbacks<NoteModelDisplay>() {
                override fun onSwipeCompleted(
                    model: NoteModelDisplay, itemView: View, position: Int,
                    direction: Int
                ) {
                    sharedViewModel.deleteNote(model.note)
                }
            })

    }
    override fun onSelectedItem(noteId: String) {
        if(noteId.isEmpty()) return

        val action = HomeFragmentDirections.actionHomeFragmentToAddNoteFragment(noteId)
        findNavController().navigate(action)
    }

    override fun onMultipleSelectionEnabled(noteId: String) {
        //todo contextual multiple deletion of notes.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
