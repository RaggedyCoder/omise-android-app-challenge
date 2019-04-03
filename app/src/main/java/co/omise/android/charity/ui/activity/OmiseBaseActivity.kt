package co.omise.android.charity.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import co.omise.android.charity.util.alertDialog
import co.omise.android.charity.util.createProgressDialog
import co.omise.android.charity.util.showShortSnackbar
import co.omise.android.charity.util.showShortToast
import co.omise.android.charity.viewmodel.OmiseBaseViewModel
import com.go.app.util.ActivityStartType
import com.go.app.util.AlertType
import com.go.app.util.ProgressDialogAction.HIDE
import com.go.app.util.ProgressDialogAction.SHOW
import com.google.android.material.textfield.TextInputLayout

abstract class OmiseBaseActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_KEY = "REQUEST_CODE"
        const val DEFAULT_REQUEST_CODE = 1001
    }

    private val progressDialog: ProgressDialog by createProgressDialog()

    private val activityViewModel: OmiseBaseViewModel by lazy {
        createViewModel()
    }

    abstract fun createViewModel(): OmiseBaseViewModel

    @Suppress("UNCHECKED_CAST")
    fun <T : OmiseBaseViewModel> getViewModel(): T = activityViewModel as T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAlertMessage()
        observeProgressDialogMessage()
        observeActivitySwitch()
        observeActivityResult()
        observeInputErrorMessage()
    }

    private fun observeAlertMessage() {
        activityViewModel.alertMessageLiveData.observe(this, Observer { alertMessageInfo ->
            alertMessageInfo?.let {
                val alertType = it.first
                val (alertTitle, alertMessage) = it.second
                val (positiveTitle, positiveAction) = it.third.first ?: Pair(null, null)
                val (negativeTitle, negativeAction) = it.third.second ?: Pair(null, null)
                when (alertType) {
                    AlertType.Toast -> {
                        showShortToast(alertMessage)
                    }
                    AlertType.Snackbar -> {
                        showShortSnackbar(alertMessage)
                    }
                    AlertType.Dialog -> {
                        val alertDialog = alertDialog {
                            setTitle(alertTitle)
                            setMessage(alertMessage)
                            setPositiveButton(positiveTitle, positiveAction)
                            setNegativeButton(negativeTitle, negativeAction)
                            setCancelable(false)
                        }
                        alertDialog.show()
                    }
                }
            }
        })
    }

    private fun observeProgressDialogMessage() {
        activityViewModel.progressDialogMessageLiveData.observe(this, Observer { progressDialogInfo ->
            progressDialogInfo?.let {
                val (action, data) = it
                when (action) {
                    SHOW -> progressDialog.cancel()
                    HIDE -> data?.let { nonNullData ->
                        val (title, message, isCancelable) = nonNullData
                        progressDialog.setCancelable(isCancelable)
                        if (!title.isNullOrBlank())
                            progressDialog.setTitle(title)
                        progressDialog.setMessage(message)
                        progressDialog.show()
                    }
                }
            }
        })
    }

    private fun observeActivitySwitch() {
        activityViewModel.activitySwitchLiveData.observe(this, Observer { activitySwitchInfo ->
            activitySwitchInfo?.let {
                val (switchType, intent, shouldFinishActivity) = it
                when (switchType) {
                    ActivityStartType.SwitchActivity -> {
                        startActivity(intent)
                    }
                    ActivityStartType.SwitchActivityForResult -> {
                        val requestCode = intent.getIntExtra(REQUEST_CODE_KEY, DEFAULT_REQUEST_CODE)
                        startActivityForResult(intent, requestCode)
                    }
                }
                if (shouldFinishActivity) {
                    finish()
                }
            }
        })
    }

    private fun observeActivityResult() {
        activityViewModel.activityResultLiveData.observe(this, Observer {
            if (it != null) {
                val (resultCode, data) = it
                setResult(resultCode, data)
                finish()
            }
        })
    }

    private fun observeInputErrorMessage() {
        activityViewModel.inputErrorMessageLiveData.observe(this, Observer { errorInputMessage ->
            errorInputMessage.let {
                val (viewId, errorMessage) = it
                val view: View = findViewById(viewId)
                when (view) {
                    is EditText -> {
                        view.error = errorMessage
                    }
                    is TextInputLayout -> {
                        view.error = errorMessage
                    }
                }
                if (!errorMessage.isNullOrBlank()) {
                    view.requestFocus()
                }
            }
        })
    }
}