package ge.kinseed.taskapp.main.presentation.currency_selector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.kinseed.taskapp.databinding.ListItemCurrencyBinding

class CurrencySelectorAdapter(
    private val onCurrencySelected: (String) -> Unit
) : ListAdapter<String, CurrencySelectorAdapter.CurrencyViewHolder>(
    MyItemDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CurrencyViewHolder(
        ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onCurrencySelected
    )

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    class CurrencyViewHolder(
        private val binding: ListItemCurrencyBinding,
        private val onCurrencySelected: (String) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bindData(currency: String) {
            binding.currency.text = currency
            binding.root.setOnClickListener {
                onCurrencySelected(currency)
            }
        }
    }
}

class MyItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}