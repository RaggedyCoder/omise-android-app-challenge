package co.omise.android.charity.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun Context.showShortToast(resId: Int) =
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()

fun Context.showShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Fragment.showShortToast(resId: Int) =
    activity?.showShortToast(resId)

fun Fragment.showShortToast(message: String) =
    activity?.showShortToast(message)

fun Context.showLongToast(resId: Int) =
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()

fun Context.showLongToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Fragment.showLongToast(resId: Int) =
    activity?.showLongToast(resId)

fun Fragment.showLongToast(message: String) =
    activity?.showLongToast(message)

fun Activity.showShortSnackbar(resId: Int) =
    showShortSnackbar(getString(resId))

fun Activity.showShortSnackbar(message: String) = Snackbar.make(
    findViewById<View>(android.R.id.content),
    message, Snackbar.LENGTH_SHORT
).show()

fun Activity.showLongSnackbar(resId: Int) =
    showShortSnackbar(getString(resId))

fun Activity.showLongSnackbar(message: String) = Snackbar
    .make(findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_LONG).show()

fun Activity.showInfSnackbar(resId: Int) =
    showShortSnackbar(getString(resId))

fun Activity.showInfSnackbar(message: String) = Snackbar
    .make(findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE).show()

fun Fragment.showShortSnackbar(resId: Int) =
    showShortSnackbar(getString(resId))

fun Fragment.showShortSnackbar(message: String) = Snackbar.make(
    view?.findViewById(android.R.id.content)!!,
    message, Snackbar.LENGTH_SHORT
).show()

fun Fragment.showLongSnackbar(resId: Int) =
    showShortSnackbar(getString(resId))

fun Fragment.showLongSnackbar(message: String) = Snackbar
    .make(view?.findViewById(android.R.id.content)!!, message, Snackbar.LENGTH_LONG).show()

fun Fragment.showInfSnackbar(resId: Int) =
    showShortSnackbar(getString(resId))

fun Fragment.showInfSnackbar(message: String) = Snackbar
    .make(view?.findViewById(android.R.id.content)!!, message, Snackbar.LENGTH_INDEFINITE).show()

fun createProgressDialog()
        : ReadOnlyProperty<Activity, ProgressDialog> = required(progressDialogCreator)

fun Fragment.createProgressDialog()
        : ReadOnlyProperty<Fragment, ProgressDialog> = required(progressDialogCreator)

fun Activity.alertDialog(block: AlertDialog.Builder.() -> Unit) =
    AlertDialog.Builder(this).apply(block).create()

private val progressDialogCreator: Activity.() -> ProgressDialog?
    get() = { ProgressDialog(this) }

private val Fragment.progressDialogCreator: Fragment.() -> ProgressDialog?
    get() = { ProgressDialog(this.activity) }

private fun cantCreateProgressDialog(desc: KProperty<*>): Nothing =
    throw IllegalStateException("Can't create progress dialog for '${desc.name}'")


@Suppress("UNCHECKED_CAST")
private fun <T> required(finder: T.() -> ProgressDialog?) = Lazy { t: T, desc ->
    t.finder() ?: cantCreateProgressDialog(desc)
}

private class Lazy<in T, out V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}

fun Context.isOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo ?: return false
    return netInfo.isConnected
}

fun Activity.hideKeyboard() {
    val view = currentFocus ?: return
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val view = activity?.currentFocus ?: return
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard() {
    val view = currentFocus ?: return
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.showKeyboard() {
    val view = activity?.currentFocus ?: return
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}