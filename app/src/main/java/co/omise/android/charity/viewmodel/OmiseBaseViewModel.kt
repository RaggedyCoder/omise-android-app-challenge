package co.omise.android.charity.viewmodel

import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omise.android.charity.OmiseApplication
import co.omise.android.charity.util.isOnline
import com.go.app.util.ActivityStartType
import com.go.app.util.AlertType
import com.go.app.util.ProgressDialogAction

abstract class OmiseBaseViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * first -> alert type
     * second -> title & message
     * third -> positive button title & action, negative button title & action
     */
    val alertMessageLiveData: LiveData<Triple<AlertType, Pair<String, String>,
            Pair<Pair<String?, ((dialog: DialogInterface, which: Int) -> Unit)?>?,
                    Pair<String?, ((dialog: DialogInterface, which: Int) -> Unit)?>?>>> =
        MutableLiveData()

    /**
     * first -> title
     * second -> message
     * third -> isCancelable
     */
    val progressDialogMessageLiveData: LiveData<Pair<ProgressDialogAction, Triple<String?, String, Boolean>?>> =
        MutableLiveData()

    /**
     * says if there is any internet connection or not
     */
    val internetConnectionLiveData: LiveData<Boolean> = MutableLiveData()

    /**
     * first -> type of starting the activity
     * second -> intent of the activity
     * third -> finish flag for calling activity
     */
    val activitySwitchLiveData: LiveData<Triple<ActivityStartType, Intent, Boolean>> =
        MutableLiveData()

    /**
     * first -> activity result code
     * second -> activity result data
     */
    val activityResultLiveData: LiveData<Pair<Int, Intent?>> = MutableLiveData()

    /**
     * first -> view id
     * second -> message
     */
    val inputErrorMessageLiveData: LiveData<Pair<Int, String?>> = MutableLiveData()

    private var noInternetRetryAction: (() -> Unit)? = null

    protected fun getString(stringResId: Int) = getApplication<Application>().getString(stringResId)

    fun showDialogAlert(
        title: String,
        message: String,
        positionButtonTitle: String? = null,
        positionButtonAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
        negativeButtonTitle: String? = null,
        negativeButtonAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null
    ) {
        (alertMessageLiveData as MutableLiveData).value =
            Triple(
                AlertType.Dialog, Pair(title, message),
                Pair(
                    Pair(positionButtonTitle, positionButtonAction),
                    Pair(negativeButtonTitle, negativeButtonAction)
                )
            )
    }

    private fun setInternetConnectionStatus(hasInternetConnection: Boolean) {
        (internetConnectionLiveData as MutableLiveData).value = hasInternetConnection
    }

    fun showProgressDialog(title: String?, message: String, isCancelable: Boolean = true) {
        (progressDialogMessageLiveData as MutableLiveData).value =
            ProgressDialogAction.SHOW to Triple(title, message, isCancelable)
    }

    fun hideProgressDialog() {
        (progressDialogMessageLiveData as MutableLiveData).value = ProgressDialogAction.HIDE to null
    }

    fun switchActivity(activityStartType: ActivityStartType, intent: Intent, shouldFinish: Boolean) {
        (activitySwitchLiveData as MutableLiveData).value = Triple(activityStartType, intent, shouldFinish)
    }

    fun finishActivity(resultCode: Int, data: Intent? = null) {
        (activityResultLiveData as MutableLiveData).value = Pair(resultCode, data)
    }

    fun setNoInternetRetry(noInternetRetryAction: () -> Unit) {
        this.noInternetRetryAction = noInternetRetryAction
        setInternetConnectionStatus(false)
    }

    fun performNoInternetFailedAction() {
        if (getApplication<OmiseApplication>().isOnline()) {
            setInternetConnectionStatus(true)
            noInternetRetryAction?.invoke()
        }
    }

    protected fun showInputError(viewId: Int, errorMessage: String) {
        (inputErrorMessageLiveData as MutableLiveData).value = viewId to errorMessage
    }

    protected fun clearInputError() {
        val viewId = inputErrorMessageLiveData.value?.first ?: return
        (inputErrorMessageLiveData as MutableLiveData).value = viewId to null
    }
}