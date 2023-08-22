package br.com.alura.aluvery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.alura.aluvery.model.Product
import br.com.alura.aluvery.sampledata.sampleCandies
import br.com.alura.aluvery.sampledata.sampleDrinks
import br.com.alura.aluvery.sampledata.sampleProducts
import br.com.alura.aluvery.sampledata.sampleSections
import br.com.alura.aluvery.ui.components.CardProductItem
import br.com.alura.aluvery.ui.components.ProductsSection
import br.com.alura.aluvery.ui.components.SearchTextField
import br.com.alura.aluvery.ui.theme.AluveryTheme

class HomeScreenUIState(
    val sections: Map<String, List<Product>> = emptyMap(),
    val searchedProducts: List<Product> = emptyList(),
    val searchText: String = "",
    val onSearchChange: (String) -> Unit = {}
) {
    fun showAllSections(): Boolean {
        return searchText.isBlank()
    }
}

@Composable
// função stateless
fun HomeScreen(products: List<Product>) {

    val sections = mapOf(
        "Todos os produtos" to products + sampleProducts,
        "Promoções" to sampleDrinks + sampleCandies,
        "Doces" to sampleCandies,
        "Bebidas" to sampleDrinks
    )

    var text by rememberSaveable {
        mutableStateOf("")
    }

    val searchedProducts = remember(text, products) {
        if (text.isNotBlank()) {
            sampleProducts.filter {it.name.contains(text, true)} +
                    products.filter {it.name.contains(text, true)}
        } else emptyList()
    }

    // no remember usamos o products, pq é um objeto que sofre alterações
    // durante a execução do app
    val state = remember(products, text) {
        HomeScreenUIState(
            sections = sections,
            searchedProducts = searchedProducts,
            searchText = text,
            onSearchChange = { text = it }
        )
    }

    HomeScreen(state = state)
}

@Composable
// função stateful
fun HomeScreen(
    state: HomeScreenUIState = HomeScreenUIState()
) {
    Column {

        val sections = state.sections
        val text = state.searchText
        val searchedProducts = state.searchedProducts

        SearchTextField(searchText = text, state.onSearchChange)

        LazyColumn(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // o contentPadding substitui os Spacer abaixo
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (state.showAllSections()) {
                //        item { Spacer(Modifier) }
                sections.forEach {
                    val title = it.key
                    val products = it.value
                    item {
                        ProductsSection(title = title, products = products)
                    }
                }
                //        item { Spacer(Modifier) }
            } else {
                items(searchedProducts) {
                    CardProductItem(product = it, Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    AluveryTheme {
        Surface {
            HomeScreen(HomeScreenUIState(sampleSections))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenSearchTextPreview() {
    AluveryTheme {
        Surface {
            HomeScreen(state = HomeScreenUIState(searchText = "a", sections = sampleSections))
        }
    }
}