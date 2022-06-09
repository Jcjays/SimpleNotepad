
package com.example.simplenotepad


import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.simplenotepad.arch.NoteViewModel
import com.example.simplenotepad.room.NoteDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: NoteViewModel by viewModels()
        viewModel.init(NoteDatabase.getDatabase(this))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

//        val fab = findViewById<FloatingActionButton>(R.id.HomeFab)
//
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
//                fab.isVisible = true
//            } else {
//                fab.isGone = true
//            }
//        }
//
//        fab.setOnClickListener {
//            navController.navigate(R.id.action_homeFragment_to_addNoteFragment)
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}