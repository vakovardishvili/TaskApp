package ge.kinseed.taskapp.main.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ge.kinseed.taskapp.R
import ge.kinseed.taskapp.databinding.ListItemBalanceBinding
import ge.kinseed.taskapp.main.presentation.model.Amount
import ge.kinseed.taskapp.util.formatAmount

class BalanceAdapter : ListAdapter<Amount, BalanceAdapter.AmountViewHolder>(
    MyItemDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AmountViewHolder(
        ListItemBalanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: AmountViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }


    class AmountViewHolder(
        private val binding: ListItemBalanceBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bindData(amount: Amount) {
            binding.root.text = itemView.context.getString(
                R.string.amount_list_item,
                amount.amount.formatAmount(),
                amount.currency
            )
        }
    }
}

class MyItemDiffCallback : DiffUtil.ItemCallback<Amount>() {
    override fun areItemsTheSame(oldItem: Amount, newItem: Amount): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Amount, newItem: Amount): Boolean {
        return oldItem == newItem
    }
}