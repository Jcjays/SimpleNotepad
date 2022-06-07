package com.example.simplenotepad

import android.app.Notification
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.example.simplenotepad.arch.BaseApplication
import com.example.simplenotepad.databinding.FragmentHomeBinding
import com.example.simplenotepad.model.HomeEpoxyController
import com.example.simplenotepad.model.NoteModelDisplay
import com.example.simplenotepad.room.NoteEntity


class HomeFragment() : BaseApplication(), IClickableState {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeEpoxyController = HomeEpoxyController(this)
    private var itemsToDelete: ArrayList<String> = ArrayList()
    private var actionMode : ActionMode? = null

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

        sharedViewModel.onSelectionModeEnable.observe(viewLifecycleOwner){
            homeEpoxyController.isSelectionModeEnabled = it
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

    override fun onMultipleSelectionEnabled(noteId: String){
        sharedViewModel.onSelectionModeEnable.postValue(true)

        val callback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mainActivity.menuInflater.inflate(R.menu.contextual_app_bar_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item?.itemId) {
                    R.id.delete -> {
                        if(itemsToDelete.isNotEmpty()){
                            sharedViewModel.deleteNotes(itemsToDelete)
                            Toast.makeText(requireContext(), "${itemsToDelete.size} Deleted", Toast.LENGTH_SHORT).show()
                        }
                        actionMode?.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                sharedViewModel.onSelectionModeEnable.value = false
                itemsToDelete.clear()
                actionMode = null
            }
        }

        if(actionMode == null) actionMode = mainActivity.startActionMode(callback)

        if(itemsToDelete.contains(noteId)) itemsToDelete.remove(noteId) else itemsToDelete.add(noteId)

        actionMode?.title = "${itemsToDelete.size} selected"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
