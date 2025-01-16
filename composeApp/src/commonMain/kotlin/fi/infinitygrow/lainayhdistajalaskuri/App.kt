package fi.infinitygrow.lainayhdistajalaskuri

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import lainayhdistajalaskuri.composeapp.generated.resources.Res
import lainayhdistajalaskuri.composeapp.generated.resources.calculator_title
import lainayhdistajalaskuri.composeapp.generated.resources.current_monthly_payment


@Composable
@Preview
fun App() {
    MaterialTheme {

        Column {
            Text(
                text = stringResource(Res.string.calculator_title),
                style = MaterialTheme.typography.headlineMedium
            )
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