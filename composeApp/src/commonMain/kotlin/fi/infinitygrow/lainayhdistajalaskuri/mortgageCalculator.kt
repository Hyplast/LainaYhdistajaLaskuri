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
import androidx.compose.ui.graphics.Color
import lainayhdistajalaskuri.composeapp.generated.resources.Res
import lainayhdistajalaskuri.composeapp.generated.resources.add_loan
import lainayhdistajalaskuri.composeapp.generated.resources.balance
import lainayhdistajalaskuri.composeapp.generated.resources.break_even_point
import lainayhdistajalaskuri.composeapp.generated.resources.closing_costs
import lainayhdistajalaskuri.composeapp.generated.resources.current_balance
import lainayhdistajalaskuri.composeapp.generated.resources.current_monthly_payment
import lainayhdistajalaskuri.composeapp.generated.resources.current_mortgage
import lainayhdistajalaskuri.composeapp.generated.resources.current_rate
import lainayhdistajalaskuri.composeapp.generated.resources.current_term
import lainayhdistajalaskuri.composeapp.generated.resources.loan
import lainayhdistajalaskuri.composeapp.generated.resources.monthly_payment
import lainayhdistajalaskuri.composeapp.generated.resources.monthly_savings
import lainayhdistajalaskuri.composeapp.generated.resources.months
import lainayhdistajalaskuri.composeapp.generated.resources.new_monthly_payment
import lainayhdistajalaskuri.composeapp.generated.resources.new_mortgage
import lainayhdistajalaskuri.composeapp.generated.resources.new_rate
import lainayhdistajalaskuri.composeapp.generated.resources.new_term
import lainayhdistajalaskuri.composeapp.generated.resources.rate
import lainayhdistajalaskuri.composeapp.generated.resources.results
import lainayhdistajalaskuri.composeapp.generated.resources.term
import lainayhdistajalaskuri.composeapp.generated.resources.total_interest_savings
import lainayhdistajalaskuri.composeapp.generated.resources.years
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
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

// First, let's create a data class to represent each loan
data class Loan(
    val balance: Double,
    val rate: Double,
    val term: Int
) {
    fun calculateMonthlyPayment(): Double {
        val monthlyRate = rate / 12 / 100
        val numberOfPayments = term * 12
        return balance * (monthlyRate * (1 + monthlyRate).pow(numberOfPayments)) /
                ((1 + monthlyRate).pow(numberOfPayments) - 1)
    }
}

// Add a function to calculate total monthly payment
fun calculateTotalMonthlyPayment(loans: List<Loan>): Double {
    return loans.sumOf { it.calculateMonthlyPayment() }
}

// UI for adding loans
@Composable
fun AddLoanForm(onAddLoan: (Loan) -> Unit) {
    var balance by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var term by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = balance,
            onValueChange = { balance = it },
            label = { Text(stringResource(Res.string.current_balance)) }
        )
        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text(stringResource(Res.string.current_rate)) }
        )
        OutlinedTextField(
            value = term,
            onValueChange = { term = it },
            label = { Text(stringResource(Res.string.current_term)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val loan = Loan(
                balance = balance.toDoubleOrNull() ?: 0.0,
                rate = rate.toDoubleOrNull() ?: 0.0,
                term = term.toIntOrNull() ?: 0
            )
            onAddLoan(loan)
            balance = ""
            rate = ""
            term = ""
        }) {
            Text(stringResource(Res.string.add_loan)) // Add Loan
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun MortgageCalculator() {
    var currentLoan by remember { mutableStateOf(CurrentLoan()) }
    var newLoan by remember { mutableStateOf(NewLoan()) }
    var results by remember { mutableStateOf(calculateResults(currentLoan, newLoan)) }
    val currentLoans = remember { mutableStateListOf<Loan>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Display current loans
        currentLoans.forEachIndexed { index, loan ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF57706)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    //Text("Loan ${index + 1}")
                    Text("${stringResource(Res.string.loan)}: ${index + 1}", color = Color.White)
                    Text("${stringResource(Res.string.balance)}: €${loan.balance}")
                    Text("${stringResource(Res.string.rate)}: ${loan.rate}%")
                    Text("${stringResource(Res.string.term)}: ${loan.term} ${stringResource(Res.string.years)}")
                    Text("${stringResource(Res.string.monthly_payment)}: €")
                }
            }
        }

        // Add new loan form
        AddLoanForm { newLoan ->
            currentLoans.add(newLoan)
        }

        // Display total monthly payment
        if (currentLoans.isNotEmpty()) {
            val totalMonthly = calculateTotalMonthlyPayment(currentLoans)
            Text(
                stringResource(Res.string.current_monthly_payment),
                //"Total Monthly Payment: $",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
//                Text(
//                    text = stringResource(Res.string.calculator_title),//"Mortgage Refinance Calculator",
//                    style = MaterialTheme.typography.titleMedium
//                )

                //Spacer(modifier = Modifier.height(16.dp))

                // Current Mortgage Section
                Text(
                    text = stringResource(Res.string.current_mortgage),//"Current Mortgage",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumberInput(
                    label = stringResource(Res.string.current_balance),//"Current Balance ($)",
                    value = currentLoan.balance,
                    onValueChange = { 
                        currentLoan = currentLoan.copy(balance = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = stringResource(Res.string.current_rate),//"Current Rate (%)",
                    value = currentLoan.rate,
                    onValueChange = { 
                        currentLoan = currentLoan.copy(rate = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = stringResource(Res.string.current_term),//"Current Term (years)",
                    value = currentLoan.term.toDouble(),
                    onValueChange = { 
                        currentLoan = currentLoan.copy(term = it.toInt())
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // New Mortgage Section
                Text(
                    text = stringResource(Res.string.new_mortgage),//"New Mortgage",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumberInput(
                    label = stringResource(Res.string.new_rate),//"New Rate (%)",
                    value = newLoan.rate,
                    onValueChange = { 
                        newLoan = newLoan.copy(rate = it)
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = stringResource(Res.string.new_term),//"New Term (years)",
                    value = newLoan.term.toDouble(),
                    onValueChange = { 
                        newLoan = newLoan.copy(term = it.toInt())
                        results = calculateResults(currentLoan, newLoan)
                    }
                )

                NumberInput(
                    label = stringResource(Res.string.closing_costs),//"Closing Costs ($)",
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
                            text = stringResource(Res.string.results),//"Results",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Current Monthly Payment:", "$%.2f".format(results.currentMonthlyPayment))
                        //ResultRow("Current Monthly Payment:", "$${(results.currentMonthlyPayment * 100).roundToInt() / 100.0}")
                        ResultRow(Res.string.current_monthly_payment, "€${(results.currentMonthlyPayment * 100).roundToInt() / 100.0}")
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("New Monthly Payment:", "$%.2f".format(results.newMonthlyPayment))
                        //ResultRow("New Monthly Payment:", "$${(results.newMonthlyPayment * 100).roundToInt() / 100.0}")
                        ResultRow(Res.string.new_monthly_payment, "€${(results.newMonthlyPayment * 100).roundToInt() / 100.0}")
                        //ResultRow("Monthly Savings:", "$${(results.monthlySavings * 100).roundToInt() / 100.0}")
                        ResultRow(Res.string.monthly_savings, "€${(results.monthlySavings * 100).roundToInt() / 100.0}")
                        //ResultRow("Total Interest Savings:", "$${(results.totalInterestSavings * 100).roundToInt() / 100.0}")
                        ResultRow(Res.string.total_interest_savings, "€${(results.totalInterestSavings * 100).roundToInt() / 100.0}")
                        //ResultRow("Break-even Point:", "${results.breakEvenMonths} months")
                        ResultRow(Res.string.break_even_point, "${results.breakEvenMonths} ${stringResource(Res.string.months)}")



                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Monthly Savings:", "$%.2f".format(results.monthlySavings))
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Total Interest Savings:", "$%.2f".format(results.totalInterestSavings))
                        //fi.infinitygrow.lainayhdistajalaskuri.ResultRow("Break-even Point:", "${results.breakEvenMonths} months")
                    }
                }
            }
        }
        Text("https://hae-lainaa.com/tietosuojaseloste/")
        Text("Copyright © 2025 Hae-lainaa.com")
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

//@Composable
//fun ResultRow(label: String) {
//    Text(StringResources.getString(label))
//}


@Composable
private fun ResultRow(labelResourceId: StringResource, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //Text(text = label)
        Text(stringResource(labelResourceId))//StringResources.getString(label))
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
