package br.com.alura.aluvery.ui.states

data class ProductFormScreenUIState(
    val url: String = "",
    val name: String = "",
    val price: String = "",
    val isPriceError: Boolean = false,
    val description: String = "",
    val onURLChange: (String) -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onPriceChange: (String) -> Unit = {},
    val onDescChange: (String) -> Unit = {}
) {
    fun showImage(): Boolean {
        return url.isNotBlank()
    }
}