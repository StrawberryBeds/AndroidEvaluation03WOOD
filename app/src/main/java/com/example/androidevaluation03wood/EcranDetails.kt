package com.example.androidevaluation03wood

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun EcranDetails(viewModel: ViewModelTransactions, navController: NavHostController, transactionID: Int) {
    val transaction = viewModel.transactions.find { it.idTransaction == transactionID }

    var modifieNomTransaction by remember { mutableStateOf("${transaction?.nomTransaction}") }
    var modifieDescriptionTransaction by remember { mutableStateOf("${transaction?.descriptionTransaction}") }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Détails de la tâche", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.padding(8.dp))

            if (transaction != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        modifieNomTransaction,
                        onValueChange = { modifieNomTransaction = it },
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Gray)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        modifieDescriptionTransaction,
                        onValueChange = { modifieDescriptionTransaction = it },
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Gray)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (modifieNomTransaction.isNotBlank()) {
                                viewModel.modifieTransaction(
                                    idTransaction = transaction.idTransaction,
                                    modifieNomTransaction,
                                    modifieDescriptionTransaction
                                )
                                modifieNomTransaction = ""
                                modifieDescriptionTransaction = ""
                            }
                            navController.navigate("ecran_transactions")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                    ) {
                        Text("Sauvegarder les modifications")
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { viewModel.toggleTransaction(transaction.idTransaction) }) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "Terminée",
                            tint = if (transaction.estRevenu) Color.Green else Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    IconButton(onClick = {
                        viewModel.supprimeTransaction(idTransaction = transaction.idTransaction)
                        navController.navigate("ecran_transactions")
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Retour au liste des Transactions")
                    }
                }
            }
        }
    }
}