package br.com.alura.aluvery.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.alura.aluvery.R
import br.com.alura.aluvery.model.Product
import br.com.alura.aluvery.ui.theme.AluveryTheme
import coil.compose.AsyncImage
import java.math.BigDecimal

class ProductFormScreenUIState(
    val url: String = "",
    val name: String = "",
    val price: String = "",
    val isPriceError: Boolean = false,
    val description: String = "",
    val onURLChange: (String) -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onPriceChange: (String) -> Unit = {},
    val onDescChange: (String) -> Unit = {},
    val onSaveClick: () -> Unit = {},
) {
    fun showImage(): Boolean {
        return url.isNotBlank()
    }
}

@Composable
fun ProductFormScreen(onSave: (Product) -> Unit = {}) {

    var url by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isPriceError by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }

//    onSaveClick: (Product) -> Unit = {}

    val state = remember(url, name, price, description) {
        ProductFormScreenUIState(
            url = url,
            name = name,
            price = price,
            description = description,
            onURLChange = { url = it },
            onNameChange = { name = it },
            onPriceChange = {
                isPriceError = try {
                    BigDecimal(it)
                    false
                } catch (e: IllegalArgumentException) {
                    it.isNotEmpty()
                }
                price = it
            },
            onDescChange = { description = it },
            onSaveClick = {
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
                onSave(product)
            }
        )
    }

    ProductFormScreen(state = state)
}

@Composable
fun ProductFormScreen(state: ProductFormScreenUIState = ProductFormScreenUIState()) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val url = state.url
        val name = state.name
        val price = state.price
        val description = state.description
        val isPriceError = state.isPriceError

        Spacer(modifier = Modifier)

        Text(text = "Criando o produto", Modifier.fillMaxWidth(), fontSize = 28.sp)


        if (state.showImage()) {
            AsyncImage(
                model = url,
                contentDescription = "imagem do produto",
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder)
            )
        }
        TextField(
            value = url,
            onValueChange = state.onURLChange,
            Modifier.fillMaxWidth(),
            label = {
                Text(text = "URL da imagem")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            )
        )


        TextField(
            value = name,
            onValueChange = state.onNameChange,
            Modifier.fillMaxWidth(),
            label = {
                Text(text = "Nome")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words
            )
        )


        Column {
            TextField(
                value = price,
                onValueChange = state.onPriceChange,
                Modifier.fillMaxWidth(),
                isError = isPriceError,
                label = {
                    Text(text = "Preço")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next,
                ),
            )
            if (isPriceError) {
                Text(
                    text = "Preço deve ser um número decimal",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        TextField(
            value = description,
            onValueChange = state.onDescChange,
            Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            label = {
                Text(text = "Descrição")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            )
        )

        Button(
            onClick = state.onSaveClick,
            Modifier.fillMaxWidth()
        ) {
            Text(text = "Salvar")
        }

        Spacer(modifier = Modifier)
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProductFormScreenPreview() {
    AluveryTheme {
        Surface {
            ProductFormScreen(ProductFormScreenUIState())
        }
    }
}