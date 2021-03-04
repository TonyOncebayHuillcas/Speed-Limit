package com.forward.controllerspeed.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.forward.controllerspeed.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(app_bar as Toolbar?)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_category, R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_update -> {
                Toast.makeText(this,"Update", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_logout->{
                Toast.makeText(this,"Salir", Toast.LENGTH_SHORT).show()

//                MaterialAlertDialogBuilder(this)
//                    .setTitle(getString(R.string.alert_dialog_title))
//                    .setMessage(getString(R.string.alert_dialog_message_logout))
//                    .setNegativeButton(getString(R.string.alert_dialog_cancel),null)
//                    .setPositiveButton("Ok") { _, _ ->
//                        val result: Result<String> = LoginRepository(LoginDataSource())
//                            .logout(application)
//                        if (result is Result.Success){
//                            startLoginActivity()
//                        }else{
//                            Toast.makeText(this,
//                                getString(R.string.message_error_resource),
//                                Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                    .show()

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}
