package ge.kinseed.taskapp.main.presentation

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import ge.kinseed.taskapp.databinding.ActivityMainBinding
import ge.kinseed.taskapp.util.observeEvent
import ge.kinseed.taskapp.view.listener.OnCellClickListener

@AndroidEntryPoint
class MainActivity : ComponentActivity(), OnCellClickListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel.ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUpViews()
        observeData()
    }

    override fun onCellClick(title: String) {
        viewModel.input.sortByCell(title)
    }

    private fun setUpViews() {
        binding.crazyTable.onCellClickListener = this

        binding.buttonFilter.setOnClickListener {
            viewModel.input.filterButtonClicked()
        }
        binding.buttonClearFilter.setOnClickListener {
            binding.search.setText("")
            viewModel.input.clearFilter()
        }
        binding.search.doOnTextChanged { text, _, _, _ ->
            viewModel.input.search(text.toString())
        }
    }

    private fun observeData() = with(viewModel.output) {
        tableLiveData.observe(this@MainActivity) { data ->
            binding.crazyTable.setUpTable(data)
        }
        infoLiveData.observeEvent(this@MainActivity) { info ->
            Toast.makeText(this@MainActivity, info, LENGTH_SHORT).show()
        }
    }
}