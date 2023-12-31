package br.com.alura.aluvery.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.alura.aluvery.dao.ProductDao
import br.com.alura.aluvery.sampledata.sampleSections
import br.com.alura.aluvery.ui.screens.HomeScreen
import br.com.alura.aluvery.ui.states.HomeScreenUIState
import br.com.alura.aluvery.ui.theme.AluveryTheme
import br.com.alura.aluvery.ui.viewmodels.HomeScreenViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(onFABclick = {
                startActivity(Intent(this, ProductFormActivity::class.java))
            }) {
                val viewmodel by viewModels<HomeScreenViewModel>()
                HomeScreen(viewmodel)
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