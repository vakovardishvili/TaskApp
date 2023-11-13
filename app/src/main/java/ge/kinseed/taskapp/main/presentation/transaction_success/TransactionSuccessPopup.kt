package ge.kinseed.taskapp.main.presentation.transaction_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import ge.kinseed.taskapp.R
import ge.kinseed.taskapp.databinding.PopupTransactionSuccessBinding
import ge.kinseed.taskapp.main.presentation.model.TransactionSuccess
import ge.kinseed.taskapp.util.formatAmount

class TransactionSuccessPopup : DialogFragment() {
    private var _binding: PopupTransactionSuccessBinding? = null
    private val binding get() = _binding!!

    private val transactionSuccess: TransactionSuccess
        get() = requireArguments().getParcelable(TRANSACTION_EXTRA_DATA)!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PopupTransactionSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.desc.text = getString(
            R.string.conversion_summary,
            transactionSuccess.fromAmount.amount.formatAmount(),
            transactionSuccess.fromAmount.currency,
            transactionSuccess.toAmount.amount.formatAmount(),
            transactionSuccess.toAmount.currency,
            transactionSuccess.fee.formatAmount()
        )

        binding.buttonDone.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TRANSACTION_EXTRA_DATA = "TRANSACTION_EXTRA_DATA"

        fun newInstance(data: TransactionSuccess) = TransactionSuccessPopup().apply {
            arguments = bundleOf(
                TRANSACTION_EXTRA_DATA to data
            )
        }
    }
}