package com.example.simplenotepad.arch

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.example.simplenotepad.MainActivity

abstract class BaseApplication : Fragment() {

    protected val sharedViewModel: NoteViewModel by activityViewModels()

    protected val mainActivity: MainActivity
        get() = activity as MainActivity


    protected fun showSoftKeyboard(view: View){
        if (view.requestFocus()) {
            val imm = mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}