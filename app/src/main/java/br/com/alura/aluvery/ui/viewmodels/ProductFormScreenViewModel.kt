package br.com.alura.aluvery.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.alura.aluvery.dao.ProductDao
import br.com.alura.aluvery.model.Product
import br.com.alura.aluvery.ui.states.ProductFormScreenUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal

class ProductFormScreenViewModel : ViewModel() {

    private val dao = ProductDao()

    private val _uiState: MutableStateFlow<ProductFormScreenUIState> = MutableStateFlow(
        ProductFormScreenUIState()
    )
    val uiState get() = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                onURLChange = {
                    _uiState.value = _uiState.value.copy(url = it)
                },
                onNameChange = {
                    _uiState.value = _uiState.value.copy(name = it)
                },
                onPriceChange = {
                    val isPriceError = try {
                        BigDecimal(it)
                        false
                    } catch (e: IllegalArgumentException) {
                        it.isNotEmpty()
                    }
                    _uiState.value = _uiState.value.copy(price = it)
                    _uiState.value = _uiState.value.copy(isPriceError = isPriceError)
                },
                onDescChange = {
                    _uiState.value = _uiState.value.copy(description = it)
                }
            )
        }
    }

    fun save() {
        _uiState.value.run {
            val convertedPrice = try {
                BigDecimal(price)
            } catch (e: NumberFormatException) {
                BigDecimal.ZERO
            }
            val product = Product(
                name = name,
                image = url,
                price = convertedPrice,
                description = description
            )
            dao.save(product)
        }
    }
}