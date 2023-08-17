package br.com.alura.aluvery.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.alura.aluvery.dao.ProductDao
import br.com.alura.aluvery.sampledata.sampleCandies
import br.com.alura.aluvery.sampledata.sampleDrinks
import br.com.alura.aluvery.sampledata.sampleProducts
import br.com.alura.aluvery.sampledata.sampleSections
import br.com.alura.aluvery.ui.screens.HomeScreen
import br.com.alura.aluvery.ui.screens.HomeScreenUIState
import br.com.alura.aluvery.ui.theme.AluveryTheme

class MainActivity : ComponentActivity() {

    private val dao = ProductDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(onFABclick = {
                startActivity(Intent(this, ProductFormActivity::class.java))
            }) {
                val products = dao.products()
                val sections = mapOf(
                    "Todos os produtos" to products,
                    "Promoções" to sampleDrinks + sampleCandies,
                    "Doces" to sampleCandies,
                    "Bebidas" to sampleDrinks
                )

                var text by remember {
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
                HomeScreen(state)
            }
        }
    }
}

@Composable
fun App(onFABclick: () -> Unit = {}, content: @Composable () -> Unit = {}) {
    AluveryTheme {
        Surface {
            // paddingValues é obrigatorio no uso de Scaffold
            Scaffold(floatingActionButton = {
                FloatingActionButton(onClick = onFABclick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    content()
                }
            }
        }
    }
}


@Preview
@Composable
fun AppPreview() {
    App {
        HomeScreen(HomeScreenUIState(sampleSections))
    }
}