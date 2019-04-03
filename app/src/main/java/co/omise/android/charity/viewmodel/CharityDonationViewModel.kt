package co.omise.android.charity.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.core.text.isDigitsOnly
import co.omise.android.Client
import co.omise.android.TokenRequest
import co.omise.android.TokenRequestListener
import co.omise.android.charity.R
import co.omise.android.charity.api.call.OmiseApiCallback
import co.omise.android.charity.api.request.DonationRequest
import co.omise.android.charity.api.response.ErrorResult
import co.omise.android.charity.api.response.donation.DonationResponse
import co.omise.android.charity.api.service.DonationService
import co.omise.android.charity.di.AndroidViewModelInjection
import co.omise.android.charity.ui.fragment.CharityDonationFragment
import co.omise.android.charity.util.CardNumberValidator
import co.omise.android.models.Token
import java.util.*
import javax.inject.Inject

class CharityDonationViewModel(application: Application) : OmiseBaseViewModel(application) {

    @Inject
    lateinit var omiseClient: Client

    @Inject
    lateinit var donationService: DonationService

    init {
        AndroidViewModelInjection.inject(this)
    }

    private val currentYear = Calendar.getInstance()[Calendar.YEAR]

    val monthRange = (1..12).map {
        it.toString()
    }
    val yearRange = (currentYear..(currentYear + 12)).map {
        it.toString()
    }

    fun donateMoney(
        fullName: CharSequence?,
        donationAmount: CharSequence?,
        cardNumber: CharSequence?,
        nameOnCard: CharSequence?,
        selectedExpiryMonthPosition: Int,
        selectedExpiryYearPosition: Int,
        securityCode: CharSequence?,
        onSuccessAction: (responseBody: DonationResponse) -> Unit
    ) {
        clearInputError()
        if (fullName.isNullOrBlank()) {
            showInputError(
                CharityDonationFragment.FULL_NAME_INPUT_LAYOUT,
                getString(R.string.full_name_empty_error_message)
            )
        } else if (donationAmount.isNullOrBlank()) {
            showInputError(
                CharityDonationFragment.DONATION_AMOUNT_INPUT_LAYOUT,
                getString(R.string.donation_amount_empty_error_message)
            )
        } else if (!donationAmount.isDigitsOnly()) {
            showInputError(
                CharityDonationFragment.DONATION_AMOUNT_INPUT_LAYOUT,
                getString(R.string.donation_amount_invalid_error_message)
            )
        } else if (cardNumber.isNullOrBlank()) {
            showInputError(
                CharityDonationFragment.CARD_NUMBER_INPUT_LAYOUT,
                getString(R.string.card_number_empty_error_message)
            )
        } else if (!CardNumberValidator.validateCardNumber(cardNumber.toString())) {
            showInputError(
                CharityDonationFragment.CARD_NUMBER_INPUT_LAYOUT,
                getString(R.string.card_number_invalid_error_message)
            )
        } else if (nameOnCard.isNullOrBlank()) {
            showInputError(
                CharityDonationFragment.NAME_ON_CARD_INPUT_LAYOUT,
                getString(R.string.name_on_card_empty_error_message)
            )
        } else if (selectedExpiryMonthPosition == -1 || selectedExpiryMonthPosition >= monthRange.size) {
            showDialogAlert(
                getString(android.R.string.dialog_alert_title),
                getString(R.string.card_expiry_month_invalid_error_message),
                getString(android.R.string.ok)
            )
        } else if (selectedExpiryYearPosition == -1 || selectedExpiryYearPosition >= yearRange.size) {
            showDialogAlert(
                getString(android.R.string.dialog_alert_title),
                getString(R.string.card_expiry_year_invalid_error_message),
                getString(android.R.string.ok)
            )
        } else if (securityCode.isNullOrBlank()) {
            showInputError(
                CharityDonationFragment.SECURITY_CODE_INPUT_LAYOUT,
                getString(R.string.security_code_empty_error_message)
            )
        } else {
            showProgressDialog(getString(R.string.please_wait), getString(R.string.donation_on_progress))
            performTokenizeCardNumber(
                nameOnCard.toString(),
                cardNumber.toString(),
                monthRange[selectedExpiryMonthPosition].toInt(),
                yearRange[selectedExpiryMonthPosition].toInt(),
                securityCode.toString()
            ) {
                performDonationRequest(fullName.toString(), it.id, donationAmount.toString().toInt(), onSuccessAction)
            }
        }
    }

    private fun performDonationRequest(
        fullName: String,
        tokenId: String,
        donationAmount: Int,
        onSuccessAction: (responseBody: DonationResponse) -> Unit
    ) {
        donationService.makeDonationRequest(DonationRequest(fullName, tokenId, donationAmount))
            .enqueue(object : OmiseApiCallback<DonationResponse>() {
                override fun success(responseBody: DonationResponse) {
                    hideProgressDialog()
                    if (responseBody.success) {
                        onSuccessAction(responseBody)
                    } else {
                        showDialogAlert(
                            getString(android.R.string.dialog_alert_title),
                            responseBody.errorMessage,
                            getString(android.R.string.ok)
                        )
                    }
                }

                override fun <A : Parcelable> error(responseBody: ErrorResult<A>) {
                    hideProgressDialog()
                    when (responseBody) {
                        is ErrorResult.NetworkError -> {
                            showDialogAlert(
                                getString(android.R.string.dialog_alert_title),
                                responseBody.response.message,
                                getString(android.R.string.ok)
                            )
                        }
                        is ErrorResult.BadRequestError -> {
                            showDialogAlert(
                                getString(android.R.string.dialog_alert_title),
                                responseBody.response.errorMessage,
                                getString(android.R.string.ok)
                            )
                        }
                        is ErrorResult.UnknownError -> {
                            showDialogAlert(
                                getString(android.R.string.dialog_alert_title),
                                responseBody.response.message,
                                getString(android.R.string.ok)
                            )
                        }
                        is ErrorResult.InvalidResponseError -> {
                            showDialogAlert(
                                getString(android.R.string.dialog_alert_title),
                                responseBody.response.message,
                                getString(android.R.string.ok)
                            )
                        }
                    }
                }
            })
    }

    private fun performTokenizeCardNumber(
        nameOnCard: String,
        cardNumber: String,
        expirationMonth: Int,
        expirationYear: Int,
        securityCode: String,
        onSuccessAction: ((token: Token) -> Unit)
    ) {
        val tokenizeCardNumber = TokenRequest()
        tokenizeCardNumber.name = nameOnCard
        tokenizeCardNumber.number = cardNumber
        tokenizeCardNumber.expirationMonth = expirationMonth
        tokenizeCardNumber.expirationYear = expirationYear
        tokenizeCardNumber.securityCode = securityCode

        omiseClient.send(tokenizeCardNumber, object : TokenRequestListener {
            override fun onTokenRequestSucceed(tokenRequest: TokenRequest?, token: Token?) {
                token?.let {
                    onSuccessAction(it)
                }
            }

            override fun onTokenRequestFailed(tokenRequest: TokenRequest?, throwable: Throwable?) {
                hideProgressDialog()
                showDialogAlert(
                    title = getString(android.R.string.dialog_alert_title),
                    message = throwable?.message ?: getString(R.string.something_went_wrong),
                    positionButtonTitle = getString(android.R.string.ok)
                )
            }
        })
    }

}