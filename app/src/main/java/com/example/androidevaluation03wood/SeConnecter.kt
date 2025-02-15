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
import androidx.compose.material3.Button
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
fun SeConnecter(
    viewModelUtilisateur: ViewModelUtilisateur, navController: NavHostController
) {
    var nomUtilisateur by remember { mutableStateOf("") }
    var motDePass by remember { mutableStateOf("") }
    var erreurConnexion by remember { mutableStateOf(false) }

    var nouvelleNomUtilisateur by remember { mutableStateOf("") }
    var nouvelleMotDePass by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Connexion", style = MaterialTheme.typography.headlineMedium)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BasicTextField(
                    value = nomUtilisateur,
                    onValueChange = { nomUtilisateur = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, Gray)
                        .padding(8.dp),

                    )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                BasicTextField(
                    value = motDePass,
                    onValueChange = { motDePass = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, Gray)
                        .padding(8.dp),
                )
            }

            if (erreurConnexion) {
                Text("Identifiants incorrects", color = Color.Red)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (viewModelUtilisateur.verifierUtilisateur(nomUtilisateur, motDePass)) {
                            navController.navigate("accueil")
                        } else {
                            erreurConnexion = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Se Connecter")
                }
            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Text("Créer un compte", style = MaterialTheme.typography.bodyMedium)
//            }
//            Spacer(modifier = Modifier.padding(8.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                BasicTextField(
//                    value = nouvelleNomUtilisateur,
//                    onValueChange = { nouvelleNomUtilisateur = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Gray)
//                        .padding(8.dp)
//                )
//            }
//            Spacer(
//                modifier = Modifier.padding(8.dp)
//            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                BasicTextField(
//                    value = nouvelleMotDePass,
//                    onValueChange = { nouvelleMotDePass = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .border(1.dp, Gray)
//                        .padding(8.dp)
//                )
//            }
//            Spacer(
//                modifier = Modifier
//                    .padding(8.dp)
//            )
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Button(
//                    onClick = {
//                        if (nouvelleNomUtilisateur.isNotBlank() && nouvelleMotDePass.isNotBlank()) {
//                            viewModel.ajouteUtilisateur(nouvelleNomUtilisateur, nouvelleMotDePass)
//                            nouvelleNomUtilisateur = ""
//                            nouvelleMotDePass = ""
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 8.dp)
//                ) {
//                    Text("Creer nouveau compte")
//                }
//            }
        }
    }
}