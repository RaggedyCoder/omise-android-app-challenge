package co.omise.android.charity.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import co.omise.android.charity.R
import co.omise.android.charity.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : OmiseBaseActivity() {
    override fun createViewModel() =
        ViewModelProviders.of(this)[MainViewModel::class.java]

    private val navController by lazy {
        findNavController(R.id.fragment_navigation_container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() =
        if (navController.currentDestination?.id == R.id.charity_donation_success_fragment) {
            navController.popBackStack(R.id.charity_list_fragment, false)
        } else {
            navController.navigateUp()
        }
}