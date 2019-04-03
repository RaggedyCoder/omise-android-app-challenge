package co.omise.android.charity.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.omise.android.charity.viewmodel.LauncherViewModel
import dagger.android.AndroidInjection

class LauncherActivity : OmiseBaseActivity() {

    override fun createViewModel() = ViewModelProviders.of(this)[LauncherViewModel::class.java]

    private val launcherViewModel by lazy {
        getViewModel() as LauncherViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        launcherViewModel.showMainActivity()
    }
}