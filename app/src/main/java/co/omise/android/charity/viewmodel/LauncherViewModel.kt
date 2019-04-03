package co.omise.android.charity.viewmodel

import android.app.Application
import android.content.Intent
import co.omise.android.charity.di.AndroidViewModelInjection
import co.omise.android.charity.ui.activity.MainActivity
import com.go.app.util.ActivityStartType

class LauncherViewModel(application: Application) : OmiseBaseViewModel(application) {

    init {
        AndroidViewModelInjection.inject(this)
    }

    fun showMainActivity() {
        switchActivity(ActivityStartType.SwitchActivity, Intent(getApplication(), MainActivity::class.java), true)
    }
}