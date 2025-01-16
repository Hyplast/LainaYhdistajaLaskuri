package fi.infinitygrow.lainayhdistajalaskuri

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lainayhdistajalaskuri.composeapp.generated.resources.Hae_lainaa_logo_2
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import lainayhdistajalaskuri.composeapp.generated.resources.Res
import lainayhdistajalaskuri.composeapp.generated.resources.calculator_title
import org.jetbrains.compose.resources.painterResource


@Composable
@Preview
fun App() {
    MaterialTheme {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,  // Centers the Row horizontally in the Column
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            item {
            //Image(painterResource(Res.drawable))
            Row {
                Image(
                    painterResource(Res.drawable.Hae_lainaa_logo_2),
                    "Logo",
                    modifier = Modifier
                        .height(40.dp)  // Adjust these values as needed
                        .width(40.dp))
                Text(
                    text = stringResource(Res.string.calculator_title),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // ... rest of your UI using stringResource
            //Text(stringResource(Res.string.current_monthly_payment))
//            Text(
//                text = StringResources.getString("calculator_title"),
//                style = MaterialTheme.typography.headlineMedium
//            )
//            // Add a language toggle button
//            Button(onClick = {
//                StringResources.currentLanguage = when (StringResources.currentLanguage) {
//                    Language.ENGLISH -> Language.FINNISH
//                    Language.FINNISH -> Language.ENGLISH
//                }
//            }) {
//                Text("Switch Language")
//            }

            MortgageCalculator()
            }
        }


//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }

    }
}