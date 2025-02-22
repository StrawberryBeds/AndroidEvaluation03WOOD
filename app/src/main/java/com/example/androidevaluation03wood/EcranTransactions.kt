package com.example.androidevaluation03wood

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun EcranTransactions(viewModel: ViewModelTransactions, navController: NavHostController) {

    var nouvelleNomTransaction by remember { mutableStateOf("") }
    var nouvelleDescriptionTransaction by remember { mutableStateOf("") }
    var estRevenu by remember { mutableStateOf(false) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BasicTextField(
                    value = nouvelleNomTransaction,
                    onValueChange = { nouvelleNomTransaction = it },
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
                BasicTextField(
                    value = nouvelleDescriptionTransaction,
                    onValueChange = { nouvelleDescriptionTransaction = it },
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
                        if (nouvelleNomTransaction.isNotBlank()) {
                            viewModel.ajouteTransaction(nouvelleNomTransaction, nouvelleDescriptionTransaction)
                            nouvelleNomTransaction = ""
                            nouvelleDescriptionTransaction = ""
                            estRevenu = false
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
                items(items = viewModel.transactions) { transaction ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.toggleTransaction(transaction.idTransaction) }) {
                            Icon(
                                Icons.Default.Done,
                                contentDescription = "Revenu ou Dépense",
                                tint = if (transaction.estRevenu) Color.Green else Red
                            )
                        }
                        IconButton(onClick = { navController.navigate("ecran_details/${transaction.idTransaction}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                        Text(transaction.nomTransaction, modifier = Modifier.weight(1f))

                        IconButton(onClick = { viewModel.supprimeTransaction(idTransaction = transaction.idTransaction) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer")


                        }
                    }
                }
            }
        }
    }
}
