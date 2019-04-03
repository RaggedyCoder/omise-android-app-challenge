package co.omise.android.charity.di

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class AndroidViewModelInjection {

    companion object {

        @JvmStatic
        fun inject(androidViewModel: AndroidViewModel) {
            val application = androidViewModel.getApplication<Application>()
            if (application !is HasViewModelInjector) {
                throw RuntimeException(
                    "${application.javaClass.canonicalName} " +
                            "does not implement ${HasViewModelInjector::class.java.canonicalName}"
                )
            }
            val androidViewModelInjector = application.androidViewModelInjector()
            androidViewModelInjector.inject(androidViewModel)
        }
    }
}