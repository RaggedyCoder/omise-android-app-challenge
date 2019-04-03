package co.omise.android.charity.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import co.omise.android.charity.R
import co.omise.android.charity.util.toFormattedCurrency
import co.omise.android.charity.viewmodel.CharityDonationSuccessViewModel
import com.bumptech.glide.RequestManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_charity_donation_success.*
import java.util.*
import javax.inject.Inject

class CharityDonationSuccessFragment : OmiseBaseFragment() {

    @Inject
    lateinit var glideRequests: RequestManager

    private val args: CharityDonationSuccessFragmentArgs by navArgs()

    override fun createViewModel() = ViewModelProviders.of(this)[CharityDonationSuccessViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_charity_donation_success, container, false)!!

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

        donationCompleteMessageTextView.text =
            getString(
                R.string.donation_success_message,
                args.donationAmount.toFormattedCurrency(Locale("th", "TH")),
                args.charityInfo.name
            )
        makeMyDonationButton.setOnClickListener {
            view.findNavController().popBackStack(R.id.charity_list_fragment, false)
        }

    }
}