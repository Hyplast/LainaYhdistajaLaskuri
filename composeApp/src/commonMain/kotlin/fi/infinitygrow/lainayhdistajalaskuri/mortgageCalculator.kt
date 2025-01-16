package fi.infinitygrow.lainayhdistajalaskuri

import fi.infinitygrow.lainayhdistajalaskuri.MortgageCalculations.calculateResults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.pow
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt


// MortgageCalculatorState.kt
data class CurrentLoan(
    val balance: Double = 200000.0,
    val rate: Double = 5.5,
    val term: Int = 30,
    val monthlyPayment: Double = 0.0
)

data class NewLoan(
    val rate: Double = 4.5,
    val term: Int = 30,
    val closingCosts: Double = 3000.0
)

data class CalculationResults(
    val currentMonthlyPayment: Double = 0.0,
    val newMonthlyPayment: Double = 0.0,
    val monthlySavings: Double = 0.0,
    val totalInterestSavings: Double = 0.0,
    val breakEvenMonths: Int = 0
)


@Composable
fun MortgageCalculator() {
    var currentLoan by remember { mutableStateOf(CurrentLoan()) }
    var newLoan by remember { mutableStateOf(NewLoan()) }
    var results by remember { mutableStateOf(calculateResults(currentLoan, newLoan)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Mortgage Refinance Calculator",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Current Mortgage Section
                Text(
                    text = "Current Mortgage",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumberInput(
                    label = "Current Balance ($)",
                    value = currentLoan.balance,
                    onValueChange = { 
                        currentLoan = currentLoan.copy(balance = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = "Current Rate (%)",
                    value = currentLoan.rate,
                    onValueChange = { 
                        currentLoan = currentLoan.copy(rate = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = "Current Term (years)",
                    value = currentLoan.term.toDouble(),
                    onValueChange = { 
                        currentLoan = currentLoan.copy(term = it.toInt())
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // New Mortgage Section
                Text(
                    text = "New Mortgage",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumberInput(
                    label = "New Rate (%)",
                    value = newLoan.rate,
                    onValueChange = { 
                        newLoan = newLoan.copy(rate = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = "New Term (years)",
                    value = newLoan.term.toDouble(),
                    onValueChange = { 
                        newLoan = newLoan.copy(term = it.toInt())
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = "Closing Costs ($)",
                    value = newLoan.closingCosts,
                    onValueChange = { 
                        newLoan = newLoan.copy(closingCosts = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Results Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Results",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Current Monthly Payment:", "$%.2f".format(results.currentMonthlyPayment))
                        ResultRow("Current Monthly Payment:", "$${(results.currentMonthlyPayment * 100).roundToInt() / 100.0}")
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("New Monthly Payment:", "$%.2f".format(results.newMonthlyPayment))
                        ResultRow("New Monthly Payment:", "$${(results.newMonthlyPayment * 100).roundToInt() / 100.0}")
                        ResultRow("Monthly Savings:", "$${(results.monthlySavings * 100).roundToInt() / 100.0}")
                        ResultRow("Total Interest Savings:", "$${(results.totalInterestSavings * 100).roundToInt() / 100.0}")
                        ResultRow("Break-even Point:", "${results.breakEvenMonths} months")



                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Monthly Savings:", "$%.2f".format(results.monthlySavings))
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Total Interest Savings:", "$%.2f".format(results.totalInterestSavings))
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Break-even Point:", "${results.breakEvenMonths} months")
                    }
                }
            }
        }
    }
}


@Composable
private fun NumberInput(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit
) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = { 
            it.toDoubleOrNull()?.let { number -> onValueChange(number) }
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value)
    }
}

// fi.infinitygrow.lainayhdistajalaskuri.MortgageCalculations.kt
object MortgageCalculations {
    private fun calculateMonthlyPayment(principal: Double, rate: Double, years: Int): Double {
        val monthlyRate = (rate / 100) / 12
        val numberOfPayments = years * 12
        return principal * 
            (monthlyRate * (1 + monthlyRate).pow(numberOfPayments)) / 
            ((1 + monthlyRate).pow(numberOfPayments) - 1)
    }

    private fun calculateTotalInterest(principal: Double, monthlyPayment: Double, years: Int): Double {
        return (monthlyPayment * years * 12) - principal
    }

    private fun calculateBreakEven(monthlySavings: Double, closingCosts: Double): Int {
        return ceil(closingCosts / monthlySavings).toInt()
    }

    fun calculateResults(currentLoan: CurrentLoan, newLoan: NewLoan): CalculationResults {
        val currentMonthlyPayment = calculateMonthlyPayment(
            currentLoan.balance,
            currentLoan.rate,
            currentLoan.term
        )

        val newMonthlyPayment = calculateMonthlyPayment(
            currentLoan.balance,
            newLoan.rate,
            newLoan.term
        )

        val currentTotalInterest = calculateTotalInterest(
            currentLoan.balance,
            currentMonthlyPayment,
            currentLoan.term
        )

        val newTotalInterest = calculateTotalInterest(
            currentLoan.balance,
            newMonthlyPayment,
            newLoan.term
        )

        val monthlySavings = currentMonthlyPayment - newMonthlyPayment
        val totalInterestSavings = currentTotalInterest - newTotalInterest
        val breakEvenMonths = calculateBreakEven(monthlySavings, newLoan.closingCosts)

        return CalculationResults(
            currentMonthlyPayment = currentMonthlyPayment,
            newMonthlyPayment = newMonthlyPayment,
            monthlySavings = monthlySavings,
            totalInterestSavings = totalInterestSavings,
            breakEvenMonths = breakEvenMonths
        )
    }
}
