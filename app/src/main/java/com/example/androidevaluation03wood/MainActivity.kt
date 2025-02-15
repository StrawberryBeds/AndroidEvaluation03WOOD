package com.example.androidevaluation03wood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidevaluation03wood.ui.theme.AndroidEvaluation03WOODTheme

// Repositoire GitHub: https://github.com/StrawberryBeds/GestionnaireDeTachesWOOD

// Conversation avec ChatGPT : https://chatgpt.com/share/679fc2ed-1b48-8007-9977-c05e638951ff
// ChatGPT est utilisé pour (1) régler problèmes entre ViewModel et NavContoller,
// (2) MainActivity.kt (LauncheEffect),
// (3) Taches.kt (init apporteDuTaches)
// (4) Taches (fun ajouteTache)
// Voir commentaires pour plus de détails au 2, 3, et 4.

class MainActivity : ComponentActivity() {

    private val viewModelTaches: ViewModelTaches by viewModels()
    private val viewModelUtilisateur: ViewModelUtilisateur by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AndroidEvaluation03WOODTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AndroidEvaluation03WOOD(viewModelTaches, viewModelUtilisateur, navController = rememberNavController())
                    }
                }
            }
        }
    }
}

@Composable
fun AndroidEvaluation03WOOD(
    viewModelTaches: ViewModelTaches,
    viewModelUtilisateur: ViewModelUtilisateur,
    navController: NavHostController
) {

    val navController = rememberNavController()

    // Avec l'aide de ChatGPT pour le fonction LaunchedEffect
    LaunchedEffect(Unit) {
        if (!viewModelUtilisateur.estUtilisateurVerifie()) {
            navController.navigate("se_connecter")
        }
    }

    NavHost(
        navController = navController,
        startDestination = "accueil"
    ) {
        composable("se_connecter") { SeConnecter(viewModelUtilisateur, navController) }
        composable("accueil") { Accueil(viewModelUtilisateur, navController) }
        composable("ecran_taches") { EcransTaches(viewModelTaches, navController) }
        composable("ecran_details/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            taskId?.let { EcranDetails(viewModelTaches, navController, it) }
        }
    }
}





