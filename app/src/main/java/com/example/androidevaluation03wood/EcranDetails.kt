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
fun EcranDetails(viewModel: ViewModelTaches, navController: NavHostController, tacheID: Int) {
    val tache = viewModel.taches.find { it.idTache == tacheID }

    var modifieNomTache by remember { mutableStateOf("${tache?.nomTache}") }
    var modifieDescriptionTache by remember { mutableStateOf("${tache?.descriptionTache}") }

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

            if (tache != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        modifieNomTache,
                        onValueChange = { modifieNomTache = it },
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Gray)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        modifieDescriptionTache,
                        onValueChange = { modifieDescriptionTache = it },
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
                            if (modifieNomTache.isNotBlank()) {
                                viewModel.modifieTache(
                                    idTache = tache.idTache,
                                    modifieNomTache,
                                    modifieDescriptionTache
                                )
                                modifieNomTache = ""
                                modifieDescriptionTache = ""
                            }
                            navController.navigate("ecran_taches")
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
                    IconButton(onClick = { viewModel.toggleTache(tache.idTache) }) {
                        Icon(
                            Icons.Default.Done,
                            contentDescription = "Terminée",
                            tint = if (tache.estTerminee) Color.Blue else Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    IconButton(onClick = {
                        viewModel.supprimeTache(idTache = tache.idTache)
                        navController.navigate("ecran_taches")
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
                        Text("Retour au liste des Taches")
                    }
                }
            }
        }
    }
}