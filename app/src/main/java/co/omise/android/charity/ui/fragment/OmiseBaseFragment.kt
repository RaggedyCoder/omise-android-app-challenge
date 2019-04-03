package co.omise.android.charity.ui.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import co.omise.android.charity.R
import co.omise.android.charity.ui.activity.OmiseBaseActivity
import co.omise.android.charity.util.*
import co.omise.android.charity.viewmodel.OmiseBaseViewModel
import com.go.app.util.ActivityStartType
import com.go.app.util.AlertType
import com.go.app.util.ProgressDialogAction
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_omise_base.*
import kotlinx.android.synthetic.main.fragment_omise_base.view.*


abstract class OmiseBaseFragment : Fragment() {

    private val progressDialog: ProgressDialog by createProgressDialog()

    private val fragmentViewModel: OmiseBaseViewModel by lazy {
        createViewModel()
    }

    abstract fun createViewModel(): OmiseBaseViewModel

    @Suppress("UNCHECKED_CAST")
    fun <T : OmiseBaseViewModel> getViewModel(): T = fragmentViewModel as T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAlertMessage()
        observeNoNetworkAlert()
        observeProgressDialogMessage()
        observeActivitySwitch()
        observeActivityResult()
        observeInputErrorMessage()
    }

    private fun observeNoNetworkAlert() {
        fragmentViewModel.internetConnectionLiveData.observe(this, Observer {
            containerView.isVisible = it == true
            noInternetConnectionMessageViewHolder.isVisible = it == false
        })
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_omise_base, container, false) as FrameLayout
        val view = onCreateContentView(inflater, rootView, savedInstanceState)
        rootView.containerView.addView(view)
        return rootView
    }

    abstract fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retryButton.setOnClickListener {
            fragmentViewModel.performNoInternetFailedAction()
        }
        onContentViewCreated(view, savedInstanceState)
    }

    open fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun observeAlertMessage() {
        fragmentViewModel.alertMessageLiveData.observe(this, Observer { alertMessageInfo ->
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
                        val alertDialog = activity?.alertDialog {
                            setTitle(alertTitle)
                            setMessage(alertMessage)
                            setPositiveButton(positiveTitle, positiveAction)
                            setNegativeButton(negativeTitle, negativeAction)
                            setCancelable(false)
                        }
                        alertDialog?.show()
                    }
                }
            }
        })
    }

    private fun observeProgressDialogMessage() {
        fragmentViewModel.progressDialogMessageLiveData.observe(this, Observer { progressDialogInfo ->
            progressDialogInfo?.let {
                val (action, data) = it
                when (action) {
                    ProgressDialogAction.HIDE -> progressDialog.cancel()
                    ProgressDialogAction.SHOW -> data?.let { nonNullData ->
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
        fragmentViewModel.activitySwitchLiveData.observe(this, Observer { activitySwitchInfo ->
            activitySwitchInfo?.let {
                val (switchType, intent, shouldFinishActivity) = it
                when (switchType) {
                    ActivityStartType.SwitchActivity -> {
                        startActivity(intent)
                    }
                    ActivityStartType.SwitchActivityForResult -> {
                        val requestCode = intent.getIntExtra(
                            OmiseBaseActivity.REQUEST_CODE_KEY,
                            OmiseBaseActivity.DEFAULT_REQUEST_CODE
                        )
                        startActivityForResult(intent, requestCode)
                    }
                }
                if (shouldFinishActivity) {
                    activity?.finish()
                }
            }
        })
    }

    private fun observeActivityResult() {
        fragmentViewModel.activityResultLiveData.observe(this, Observer {
            if (it != null) {
                val (resultCode, data) = it
                activity?.setResult(resultCode, data)
                activity?.finish()
            }
        })
    }

    private fun observeInputErrorMessage() {
        fragmentViewModel.inputErrorMessageLiveData.observe(this, Observer { errorInputMessage ->
            errorInputMessage?.let {
                val (viewId, errorMessage) = it
                val view: View = view?.findViewById(viewId)!!
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
                    showKeyboard()
                }
            }
        })
    }
}