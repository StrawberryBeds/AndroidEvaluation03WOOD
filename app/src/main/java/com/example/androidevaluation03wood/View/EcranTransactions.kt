package com.example.androidevaluation03wood.View

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.androidevaluation03wood.Models.ViewModelTransactions

@Composable
fun EcranTransactions(viewModel: ViewModelTransactions, navController: NavHostController) {

    val transactions by viewModel.transactions.collectAsState()
    var nouvelleMontant by remember { mutableStateOf("") }
    var nouvelleCategoryTransaction by remember { mutableStateOf("") }

    val radioOptions = listOf("Revenu", "Dépense")
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    var text by remember { mutableStateOf("") }
    var doubleValue by remember { mutableStateOf<Double?>(null) }
    var error by remember { mutableStateOf(false) }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                "Nouvelle transaction",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = text,
                    onValueChange = { nouvelleMontant ->
                        text = nouvelleMontant
                        doubleValue = nouvelleMontant.toDoubleOrNull()
                        error = doubleValue == null && nouvelleMontant.isNotEmpty()

                    },
                    label = { Text("100.00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = error,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Gray)
                        .padding(8.dp)
                )
            }
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = nouvelleCategoryTransaction,
                    onValueChange = { nouvelleCategoryTransaction = it },
                    label = { Text("Categorie") },
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Gray)
                        .padding(8.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (text.isNotEmpty()) {
                            val montantDouble = text.toDoubleOrNull()
                            if (montantDouble != null)
                            {
                                viewModel.ajouteTransaction(
                                    selectedOption,
                                    montantDouble,
                                    nouvelleCategoryTransaction
                                )
                                selectedOption = radioOptions[0]
                                nouvelleMontant = ""
                                nouvelleCategoryTransaction = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    Text("Ajouter")
                }
            }
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(transactions, key = { it.idTransaction }) { transaction ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
//                        IconButton(onClick = { viewModel.toggleTransaction(transaction.idTransaction.toString()) }) {
//                            Icon(
//                                Icons.Default.Done,
//                                contentDescription = "Revenu ou Dépense",
//                                tint = if (transaction.estRevenu) Color.Green else Red
//                            )
//                        }
                        IconButton(onClick = { navController.navigate("ecran_details/${transaction.idTransaction}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                        Text("${transaction.montant}", modifier = Modifier.weight(1f))

                        IconButton(onClick = { viewModel.supprimeTransaction(idTransaction = transaction.idTransaction) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                        }
                    }
                }
            }
        }
    }
}

fun onOptionSelected(text: String) {

}
