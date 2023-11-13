package ge.kinseed.taskapp

import ge.kinseed.taskapp.main.domain.interactor.GetExchangeDataUseCase
import ge.kinseed.taskapp.main.domain.interactor.TransactionFeeHelper
import ge.kinseed.taskapp.main.presentation.MainViewModel
import ge.kinseed.taskapp.main.presentation.model.mapper.ExchangeUiMapper
import io.mockk.mockk
import org.junit.Before
import java.math.BigDecimal

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel.ViewModel
    private val testBalance = hashMapOf(
        "USD" to BigDecimal(100),
        "EUR" to BigDecimal(200),
        "GBP" to BigDecimal(300),
        "GEL" to BigDecimal(400)
    )
    private val getExchangeDataUseCase: GetExchangeDataUseCase = mockk()
    private val exchangeUiMapper: ExchangeUiMapper = mockk()
    private val transactionFeeHelper: TransactionFeeHelper = TransactionFeeHelper()

    @Before
    fun setUp() {
        viewModel = MainViewModel.ViewModel(
            getExchangeDataUseCase,
            exchangeUiMapper,
            transactionFeeHelper
        )
    }
}