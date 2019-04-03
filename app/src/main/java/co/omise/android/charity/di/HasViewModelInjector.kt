package co.omise.android.charity.di

import androidx.lifecycle.AndroidViewModel
import dagger.android.AndroidInjector

interface HasViewModelInjector {

    /** Returns an [AndroidInjector] of [AndroidViewModel]s.  */
    abstract fun androidViewModelInjector(): AndroidInjector<AndroidViewModel>
}