package co.omise.android.charity.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import co.omise.android.charity.R
import co.omise.android.charity.util.hideKeyboard
import co.omise.android.charity.viewmodel.CharityDonationViewModel
import com.bumptech.glide.RequestManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_charity_donation.*
import javax.inject.Inject

class CharityDonationFragment : OmiseBaseFragment() {

    companion object {
        const val FULL_NAME_INPUT_LAYOUT = R.id.fullNameTextInputLayout
        const val DONATION_AMOUNT_INPUT_LAYOUT = R.id.donationAmountTextInputLayout
        const val CARD_NUMBER_INPUT_LAYOUT = R.id.cardNumberTextInputLayout
        const val NAME_ON_CARD_INPUT_LAYOUT = R.id.nameOnCardTextInputLayout
        const val SECURITY_CODE_INPUT_LAYOUT = R.id.securityCodeTextInputLayout
    }

    private val args: CharityDonationFragmentArgs by navArgs()

    @Inject
    lateinit var glideRequests: RequestManager

    override fun createViewModel() = ViewModelProviders.of(this)[CharityDonationViewModel::class.java]

    private val charityDonationViewModel by lazy {
        getViewModel() as CharityDonationViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_charity_donation, container, false)!!

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        val drawable = VectorDrawableCompat.create(
            resources, R.drawable.ic_account_24dp,
            context?.theme
        )
        glideRequests.load(args.charityInfo.logoUrl)
            .placeholder(drawable)
            .error(drawable)
            .into(charityIconImageView)
        charityNameTextView.text = args.charityInfo.name

        expiryMonthSpinner.adapter =
            ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                charityDonationViewModel.monthRange
            )
        expiryYearSpinner.adapter =
            ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                charityDonationViewModel.yearRange
            )


        makeMyDonationButton.setOnClickListener(::makeMyDonation)
    }

    private fun makeMyDonation(@Suppress("UNUSED_PARAMETER") view: View) {
        hideKeyboard()
        charityDonationViewModel.donateMoney(
            fullNameTextInputEditText.text,
            donationAmountTextInputEditText.text,
            cardNumberEditText.text,
            nameOnCardTextInputEditText.text,
            expiryMonthSpinner.selectedItemPosition,
            expiryYearSpinner.selectedItemPosition,
            securityCodeTextInputEditText.text
        ) {
            view.findNavController()
                .navigate(
                    CharityDonationFragmentDirections
                        .actionGoToCharityDonationSuccessFragment(
                            donationAmountTextInputEditText.text.toString().toInt(),
                            it, args.charityInfo
                        )
                )
        }
    }
}