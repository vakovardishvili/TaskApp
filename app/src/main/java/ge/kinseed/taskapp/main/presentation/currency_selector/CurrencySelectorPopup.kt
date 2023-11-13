package ge.kinseed.taskapp.main.presentation.currency_selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import ge.kinseed.taskapp.databinding.PopupCurrencySelectorBinding

class CurrencySelectorPopup : DialogFragment() {
    private var _binding: PopupCurrencySelectorBinding? = null
    private val binding get() = _binding!!

    private val currencies: List<String> by lazy {
        requireArguments().getStringArrayList(CURRENCIES_EXTRA)!!
    }

    private val adapter = CurrencySelectorAdapter {
        setFragmentResult(
            REQUEST_KEY_SELECTED_CURRENCY,
            bundleOf(REQUEST_KEY_SELECTED_CURRENCY to it)
        )
        dismiss()
    }

    override fun onStart() {
        super.onStart()

        // Set the width and height of the dialog fragment
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.5).toInt()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PopupCurrencySelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.adapter = adapter
        adapter.submitList(currencies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY_SELECTED_CURRENCY = "REQUEST_KEY_SELECTED_CURRENCY"

        private const val CURRENCIES_EXTRA = "CURRENCIES_EXTRA"

        fun newInstance(currencies: List<String>) = CurrencySelectorPopup().apply {
            arguments = bundleOf(
                CURRENCIES_EXTRA to currencies
            )
        }
    }
}